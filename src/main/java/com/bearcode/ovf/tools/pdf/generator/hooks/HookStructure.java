package com.bearcode.ovf.tools.pdf.generator.hooks;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 20.01.14
 * Time: 19:47
 *
 * @author Leonid Ginzburg
 */
public class HookStructure {
    String hookName;
    ArrayList<String> arguments = null;

    private HookStructure() {
        arguments = new ArrayList<String>();
    }

    static public HookStructure createHookStructure( final String hook ) {
        HookStructure structure = new HookStructure();
        final int sb = hook.indexOf("(");
        final int eb = hook.lastIndexOf(")");
        if ( sb == -1 ) {
            structure.hookName = hook;
        }
        else if ( eb != -1 && sb < eb ) {
            structure.hookName = hook.substring( 0, sb );
            String args = hook.substring(sb + 1, eb);   // excluding parentheses
            Matcher quoted = Pattern.compile("\\\"[^\"]*\\\"").matcher(args);
            int start = 0;
            int end = 0;
            ArrayList<String> tokens =  new ArrayList<String>();
            while ( quoted.find( end ) ) {
                start = quoted.start();
                end = quoted.end();
                String elem = args.substring( start, end ); // including quotes
                tokens.add(elem);
            }
            if ( tokens.size() > 0 ) {
                for ( int i = 0; i < tokens.size(); i++ ) {
                    args = args.replace( tokens.get( i ), "$" + i );
                }
            }

            final String[] fields = args.split( "," );
            for ( String field : fields ) {
                field = field.trim();
                if ( field.startsWith( "$" ) ) {
                    //find string in tokens
                    try {
                        int index = Integer.parseInt( field.substring(1) );
                        field = tokens.get( index );
                    } catch (NumberFormatException e) {
                        //possible syntax error, don't care about it here
                    }
                }
                structure.arguments.add( field );
            }
        }

        return structure;
    }

    public String getHookName() {
        return hookName;
    }

/*
    public void setHookName(String hookName) {
        this.hookName = hookName;
    }
*/

    public ArrayList<String> getArguments() {
        return arguments;
    }

/*
    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }
*/
}
