package com.bearcode.ovf.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bearcode.ovf.model.common.OverseasUser;

public class SecurityContextHelper {

	public static OverseasUser getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object user = authentication.getPrincipal();
			if (user instanceof OverseasUser)
				return (OverseasUser) user;
		}
		return null;
	}

	public static void authenticateUser( OverseasUser user ) {

		UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities() );
		SecurityContextHolder.getContext().setAuthentication( t );

	}
}
