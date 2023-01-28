package com.bearcode.ovf.webservices.localelections.model;

import com.bearcode.ovf.model.eod.ElectionView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by leonid on 01.06.16.
 */
public class LocalElectionDecorator extends LocalElection implements ElectionView, Serializable {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "EE MMM d, yyyy", Locale.US );
    private static final long serialVersionUID = -5640945282782771762L;

    public static final String VIEW_STATUS = "approved";
    private static final String TO_BE_DETERMINED = "Data Unavailable";


    public LocalElectionDecorator() {
    }

    @Override
    public String getCitizenBallotRequest() {
        return findDatesOfKind( ElectionDateKind.OBRD );
    }

    @Override
    public String getCitizenBallotReturn() {
        return findDatesOfKind( ElectionDateKind.OBED );
    }

    @Override
    public String getCitizenRegistration() {
        return findDatesOfKind( ElectionDateKind.ORD );
    }

    @Override
    public String getDomesticBallotRequest() {
        return findDatesOfKind( ElectionDateKind.DBRD );
    }

    @Override
    public String getDomesticBallotReturn() {
        return findDatesOfKind( ElectionDateKind.DBED );
    }

    @Override
    public String getDomesticRegistration() {
        return findDatesOfKind( ElectionDateKind.DRD );
    }

    @Override
    public String getMilitaryBallotRequest() {
        return isUseOverseasDatesAsMilitaryDates() ? findDatesOfKind( ElectionDateKind.OBRD ) : findDatesOfKind( ElectionDateKind.MBRD );
    }

    @Override
    public String getMilitaryBallotReturn() {
        return isUseOverseasDatesAsMilitaryDates() ? findDatesOfKind( ElectionDateKind.OBED ) : findDatesOfKind( ElectionDateKind.MBED );
    }

    @Override
    public String getMilitaryRegistration() {
        return isUseOverseasDatesAsMilitaryDates() ? findDatesOfKind( ElectionDateKind.ORD ) : findDatesOfKind( ElectionDateKind.MRD );
    }

    @Override
    public String getDomesticEarlyVoting() {
        if ( !VIEW_STATUS.equalsIgnoreCase( getElectionStatus() ) ) {
            return TO_BE_DETERMINED;
        }
        return getDatesRange( ElectionDateKind.EVF, ElectionDateKind.EVT );
    }

    @Override
    public String getAbsenteeVoting() {
        if ( !VIEW_STATUS.equalsIgnoreCase( getElectionStatus() ) ) {
            return "" /*TO_BE_DETERMINED*/;
        }
        return getDatesRange( ElectionDateKind.AVF, ElectionDateKind.AVT );
    }

    private String getDatesRange( ElectionDateKind kindOne, ElectionDateKind kindTwo ) {
        List<ElectionDate> fromDates = new LinkedList<ElectionDate>();
        List<ElectionDate> toDates = new LinkedList<ElectionDate>();
        for ( ElectionDate date : getDates() ) {
            if ( date.getDateKind() == kindOne ) {
                fromDates.add( date );
            }
            if ( date.getDateKind() == kindTwo ) {
                toDates.add( date );
            }
        }
        Collections.sort( fromDates );
        Collections.sort( toDates );
        if ( fromDates.size() > 0 || toDates.size() > 0 ) {
            ElectionDate firstDate = null;
            if ( fromDates.size() > 0 ) {
                firstDate = fromDates.get( 0 );
            } else if ( toDates.size() > 0 ) {
                firstDate = toDates.get( 0 );
            }
            if ( firstDate != null &&
                    (firstDate.getDateType().getName().equalsIgnoreCase( ElectionDateType.AUTOMATIC )
                            || firstDate.getDateType().getName().equalsIgnoreCase( ElectionDateType.NOT_ALLOWED )
                            || firstDate.getDateType().getName().equalsIgnoreCase( ElectionDateType.NOT_REQUIRED )
                            || firstDate.getDateType().getName().equalsIgnoreCase( ElectionDateType.TO_BE_ANNOUNCED )) ) {
                return firstDate.getDateHumanReadable();
            }

            return String.format( "%s - %s", printCollectionMarkup( fromDates ), printCollectionMarkup( toDates ) );
        }
        return "";

    }

    @Override
    public String getDomesticNotes() {
        return getAdditionalInformation();
    }

    @Override
    public String getHeldOn() {
        if ( !VIEW_STATUS.equalsIgnoreCase( getElectionStatus() ) ) {
            return TO_BE_DETERMINED;
        }
        Date date = super.getElectionDateAsDate();
        return date != null ? dateFormat.format( date ) : "";
    }

    @Override
    public String getStateId() {
        return getState().getId();
    }

    @Override
    public String getStateName() {
        return getState().getName();
    }

    @Override
    public String getStateAbbr() {
        return getState().getShortName();
    }

    @Override
    public String getNotes() {
        return getAdditionalInformation();
    }

    @Override
    public String getTitle() {
        /*String[] nameParts = getStateName().split( " " );
        for ( String aPart : nameParts ) {
            if ( super.getTitle().contains( aPart ) ) {
                return super.getTitle();
            }
        }
        StringBuilder title = new StringBuilder( getStateName() ).append( " " ).append( super.getTitle() );
        return title.toString();*/
        return super.getTitle();
    }

    protected String findDatesOfKind( ElectionDateKind kind ) {
        if ( !VIEW_STATUS.equalsIgnoreCase( getElectionStatus() ) ) {
            return TO_BE_DETERMINED;
        }
        List<ElectionDate> dates = new ArrayList<ElectionDate>();
        for ( ElectionDate date : super.getDates() ) {
            if ( date.getDateKind() == kind ) {
                dates.add( date );
            }
        }
        Collections.sort( dates );
        return printCollectionMarkup( dates );
    }

    private String printCollectionMarkup( Collection<ElectionDate> dates ) {
        switch( dates.size() ) {
            case 0:
                return "";
            case 1:
                return dates.iterator().next().getReadableWithMarkup();
            default:
                StringBuilder markup = new StringBuilder();
                markup.append( "<ul>" );
                for ( ElectionDate date : dates ) {
                    markup.append( "<li>" ).append( date.getReadableWithMarkup() ).append( "</li>" );
                }
                markup.append( "</ul>" );
                return markup.toString();
        }
    }

    @Override
    public int compareTo( ElectionView obj ) {
        int byStateName = this.getStateName().compareTo( obj.getStateName() );
        if ( byStateName == 0 ) {

            int byName = this.getTitle().compareTo( obj.getTitle() );

            int byDate = 0;

            int byType = 0;

            if ( obj instanceof LocalElectionDecorator ) {
                LocalElectionDecorator elTwo = (LocalElectionDecorator) obj;
                if ( this.getElectionDateAsDate() != null
                        && elTwo.getElectionDateAsDate() != null ) {
                    byDate = this.getElectionDateAsDate().compareTo( elTwo.getElectionDateAsDate() );
                }

                byType = this.getSortOrder().getOrder() - elTwo.getSortOrder().getOrder();
            }


            return byType == 0 ? ( byDate == 0 ? byName : byDate ) : byType;  // sort by type, then by date, then by name
        }
        return byStateName;
    }

    public ElectionLevelSortOrder getSortOrder() {
        for ( ElectionLevelSortOrder order : ElectionLevelSortOrder.values() ) {
            Pattern pattern = Pattern.compile( order.getPattern() );
            if ( pattern.matcher( getElectionLevel().getName() ).matches() ) {  //order.getName().equalsIgnoreCase( getElectionLevel().getName() )
                return order;
            }
        }
        return ElectionLevelSortOrder.OTHER;
    }
}