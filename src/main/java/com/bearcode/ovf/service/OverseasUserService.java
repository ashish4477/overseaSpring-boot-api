package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.OverseasUserDAO;
import com.bearcode.ovf.forms.AdminStatisticsForm;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.ExtendedProfile;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.common.VoterType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 5, 2007
 * Time: 9:56:26 PM
 *
 * @author Leonid Ginzburg
 */
@Service("userService")
public class OverseasUserService {

	public static final int FIRST_OVF_ELECTION_YEAR = 2008;
	public static final int ELECTION_YEAR_INCREMENT = 4;

	@Autowired
	private OverseasUserDAO userDAO;

	@Autowired
	ZohoService zohoService;

	public OverseasUserDAO getUserDAO() {
		return userDAO;
	}

	public void makeNewUser(OverseasUser user){
		String password = user.getPassword();
		if (password != null) {
			user.setPassword(OverseasUser.encrypt(password));
			user.setScytlPassword(OverseasUser.encryptScytl(password));
		}
		saveUser(user);
		zohoService.sendContactToZoho(user);
	}

	public void updateUser(OverseasUser user){
		user = (OverseasUser) userDAO.merge(user);
		saveUser(user);
	}

	public void saveUser(OverseasUser user) {

		UserAddress currentAddress = user.getCurrentAddress();
		if (currentAddress != null && currentAddress.isEmptySpace()) {
			user.setCurrentAddress(null);
			userDAO.makeTransient(currentAddress);
		}

		UserAddress votingAddress = user.getVotingAddress();
		if (votingAddress != null && votingAddress.isEmptySpace()) {
			user.setVotingAddress(null);
			userDAO.makeTransient(votingAddress);
		}

		UserAddress forwardingAddress = user.getForwardingAddress();
		if (forwardingAddress != null && forwardingAddress.isEmptySpace()) {
			user.setForwardingAddress(null);
			userDAO.makeTransient(forwardingAddress);
		}

		UserAddress previousAddress = user.getPreviousAddress();
		if (previousAddress != null && previousAddress.isEmptySpace()) {
			user.setPreviousAddress(null);
			userDAO.makeTransient(previousAddress);
		}

		user.setLastUpdated(new Date());
		userDAO.makePersistent(user);
	}

	public void deleteUser(OverseasUser user) {
		user = (OverseasUser) userDAO.merge(user);
		userDAO.evict(user);
		userDAO.makeTransient(user);
	}

	public Collection<UserRole> findRolesByName(String userRole) {
		return userDAO.findRolesByName(userRole);
	}

	public OverseasUser findUserByName(String email) {
		try {
			return (OverseasUser) userDAO.loadUserByUsername(email, false);
		} catch (UsernameNotFoundException e) {
			return null;
		}
	}

	public Collection<UserRole> findRoles() {
		return userDAO.findRoles();
	}

	public Collection<OverseasUser> findUsers(UserFilterForm filter) {
		return userDAO.findUsers(filter, filter.createPagingInfo());
	}

	public OverseasUser findUserById(long userId) {
		return userDAO.findById(userId);
	}

	public UserRole findRoleById(long roleId) {
		return userDAO.findRoleById(roleId);
	}

	public void makeAnonymous(OverseasUser user) {
		do {
			user.makeAnonymous();
		} while (findUserByName(user.getUsername()) != null);
		user.setLastUpdated(new Date());
		userDAO.makePersistent(user);
	}

	// STATISTICS
	public int countUsers() {
		return userDAO.countUsers();
	}

	public int countUsers(UserFilterForm filter) {
		return userDAO.countUsers(filter);
	}

	public int countRealUsers() {
		return userDAO.countRealUsers();
	}

	public Map<State, Long> countUsersByState(AdminStatisticsForm form) {
		Map<State, Long> map = new LinkedHashMap<State, Long>();
		Collection stats = userDAO.countUsersByState(form.getStartRangeDate(), form.getEndRangeDate());
		for (Object entry : stats) {
			if (entry instanceof Object[]) {
				Object[] arra = (Object[]) entry;
				if (arra.length > 1) {
					if (arra[0] instanceof State && (arra[1] instanceof Long || arra[1] instanceof Integer)) {
						Long count;
						if (arra[1] instanceof Long) {
							count = (Long) arra[1];
						} else {
							count = ((Integer) arra[1]).longValue();
						}
						map.put((State) arra[0], count);
					}
				}
			}
		}
		return map;
	}

	public Collection countPdfGenerations(AdminStatisticsForm form) {
		return userDAO.countGenerations(form.getStartRangeDate(), form.getEndRangeDate());
	}

	public Collection findGenerationLog(OverseasUser user) {
		return userDAO.findGenerationLog(user);
	}

	public void evict(OverseasUser existingUser) {
		userDAO.evict(existingUser);
	}

	/**
	 * Finds the user by the MD5 encoded email address.
	 *
	 * @param emailMd5 the MD5 encoded email address.
	 * @return the matching user or <code>null</code>.
	 * @author IanBrown
	 * @version Jun 6, 2012
	 * @since Jun 6, 2012
	 */
	public OverseasUser findUserByNameMd5(final String emailMd5) {
		return (OverseasUser) getUserDAO().loadUserByUsernameMd5(emailMd5, false);
	}

	public List<Integer> findUserMembershipStat(OverseasUser user) {
		List<Integer> searchFor = new LinkedList<Integer>();
		for (int year = FIRST_OVF_ELECTION_YEAR; year <= Calendar.getInstance().get(Calendar.YEAR); year += ELECTION_YEAR_INCREMENT) {
			searchFor.add(year);
		}
		return getUserDAO().loadUserMembershipStats(user, searchFor);
	}

	public ExtendedProfile findExtendedProfile(OverseasUser user) {
		return getUserDAO().findExtendedProfile(user);
	}

	public void saveExtendedProfile(ExtendedProfile extendedProfile, VoterType voterType) {
		String updatedVoterType = Objects.isNull( voterType) ? VoterType.UNSPECIFIED.name() : voterType.name();
		extendedProfile.setVoterType(updatedVoterType);
		getUserDAO().makePersistent(extendedProfile);
		if(!StringUtils.isEmpty(extendedProfile.getVoterType())
				|| !StringUtils.isEmpty(extendedProfile.getVotingMethod()	)
				|| !StringUtils.isEmpty(extendedProfile.getPoliticalParty())){
			zohoService.updateVoterInformationToZoho(extendedProfile.getUser().getUsername(), extendedProfile.getVoterType(), extendedProfile.getVotingMethod(), extendedProfile.getPoliticalParty());
		}
	}
}
