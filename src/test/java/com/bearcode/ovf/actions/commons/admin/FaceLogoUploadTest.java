/**
 * 
 */
package com.bearcode.ovf.actions.commons.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Date;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.FacesService;

/**
 * Test for {@link FaceLogoUpload}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Dec 22, 2011
 */
public final class FaceLogoUploadTest extends EasyMockSupport {

	/**
	 * the face logo upload to test.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private FaceLogoUpload faceLogoUpload;

	/**
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private FacesService facesService;

	/**
	 * the original security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext originalSecurityContext;

	/**
	 * the security context.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext securityContext;

	/**
	 * Sets up the face logo upload for testing.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Before
	public final void setUpFaceLogoUpload() {
		setSecurityContext(createMock("SecurityContext", SecurityContext.class));
		setFacesService(createMock("FacesService", FacesService.class));
		setFaceLogoUpload(createFaceLogoUpload());
		setOriginalSecurityContext(SecurityContextHolder.getContext());
		SecurityContextHolder.setContext(getSecurityContext());
		ReflectionTestUtils.setField(getFaceLogoUpload(), "facesService",
				getFacesService());
	}

	/**
	 * Tears down the face logo upload after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@After
	public final void tearDownFaceLogoUpload() {
		setFaceLogoUpload(null);
		setFacesService(null);
		SecurityContextHolder.setContext(getOriginalSecurityContext());
		setSecurityContext(null);
		setOriginalSecurityContext(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for an empty file.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_emptyFile() {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(true);
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for the case where there is a logo, but no user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_logoNoUser() {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(
				faceConfig);
		final FaceFlowLogo logo = createMock("FaceFlowLogo", FaceFlowLogo.class);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(logo);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for the case where there is logo and a user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem getting the bytes from the file.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_logoUser() throws IOException {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(
				faceConfig);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(logo);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		logo.setUpdatedBy(user);
		logo.setUpdatedTime((Date) EasyMock.anyObject());
		logo.setFaceConfig(faceConfig);
		final String contentType = "Content Type";
		EasyMock.expect(file.getContentType()).andReturn(contentType);
		logo.setContentType(contentType);
		final byte[] bytes = "Content".getBytes();
		EasyMock.expect(file.getBytes()).andReturn(bytes);
		logo.setLogo(bytes);
		getFacesService().saveFaceLogo(logo);
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for the case where there is no logo, but there is a user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem getting the bytes from the file.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_noLogoHaveUser() throws IOException {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(
				faceConfig);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(null);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final String contentType = "Content Type";
		EasyMock.expect(file.getContentType()).andReturn(contentType);
		final byte[] bytes = "Content".getBytes();
		EasyMock.expect(file.getBytes()).andReturn(bytes);
		getFacesService().saveFaceLogo((FaceFlowLogo) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new FacesService() {

			@Override
			public final void saveFaceLogo(final FaceFlowLogo logo) {
				assertSame("The logo was updated by the user", user,
						logo.getUpdatedBy());
				assertNotNull("The logo has an updated time",
						logo.getUpdatedTime());
				assertSame("The face configuration is set in the logo",
						faceConfig, logo.getFaceConfig());
				assertEquals("The content type is set in the logo",
						contentType, logo.getContentType());
				assertSame("The content is set in the logo", bytes,
						logo.getLogo());
			}
		});
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for the case where there is no logo or user.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_noLogoOrUser() {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(
				faceConfig);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(null);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceLogoUpload#uploadLogo(org.springframework.web.multipart.MultipartFile, java.lang.Long)}
	 * for the case where reading the file fails.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem getting the bytes from the file.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUploadLogo_readFailure() throws IOException {
		final MultipartFile file = createMock("File", MultipartFile.class);
		final Long configId = 76l;
		EasyMock.expect(file.isEmpty()).andReturn(false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(
				faceConfig);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(null);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final String contentType = "Content Type";
		EasyMock.expect(file.getContentType()).andReturn(contentType);
		EasyMock.expect(file.getBytes()).andThrow(
				new IOException("Expected exception"));
		replayAll();

		final String actualUploadLogo = getFaceLogoUpload().uploadLogo(file,
				configId);

		final String expectedUploadLogo = String.format(
				"redirect:/admin/EditFaceConfig.htm?configId=%d", configId);
		assertEquals(
				"The returned string is redirects to editing the face configuration",
				expectedUploadLogo, actualUploadLogo);
		verifyAll();
	}

	/**
	 * Gets the security context.
	 * 
	 * @author IanBrown
	 * @return the security context.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	protected final SecurityContext getSecurityContext() {
		return securityContext;
	}

	/**
	 * Adds authentication to the security context.
	 * 
	 * @author IanBrown
	 * @return the authentication.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private Authentication addAuthenticationToSecurityContext() {
		final Authentication authentication = createMock("Authentication",
				Authentication.class);
		EasyMock.expect(getSecurityContext().getAuthentication())
				.andReturn(authentication).anyTimes();
		return authentication;
	}

	/**
	 * Adds the overseas user to the authentication.
	 * 
	 * @author IanBrown
	 * @param authentication
	 *            the authentication.
	 * @param user
	 *            the overseas user.
	 * @return the overseas user.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private OverseasUser addOverseasUserToAuthentication(
			final Authentication authentication, final OverseasUser user) {
		EasyMock.expect(authentication.getPrincipal()).andReturn(user)
				.anyTimes();
		return user;
	}

	/**
	 * Creates a face logo upload.
	 * 
	 * @author IanBrown
	 * @return the face logo upload.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private FaceLogoUpload createFaceLogoUpload() {
		return new FaceLogoUpload();
	}

	/**
	 * Gets the face logo upload.
	 * 
	 * @author IanBrown
	 * @return the face logo upload.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private FaceLogoUpload getFaceLogoUpload() {
		return faceLogoUpload;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the original security context.
	 * 
	 * @author IanBrown
	 * @return the original security context.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private SecurityContext getOriginalSecurityContext() {
		return originalSecurityContext;
	}

	/**
	 * Sets the face logo upload.
	 * 
	 * @author IanBrown
	 * @param faceLogoUpload
	 *            the face logo upload to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setFaceLogoUpload(final FaceLogoUpload faceLogoUpload) {
		this.faceLogoUpload = faceLogoUpload;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the original security context.
	 * 
	 * @author IanBrown
	 * @param originalSecurityContext
	 *            the original security context to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setOriginalSecurityContext(
			final SecurityContext originalSecurityContext) {
		this.originalSecurityContext = originalSecurityContext;
	}

	/**
	 * Sets the security context.
	 * 
	 * @author IanBrown
	 * @param securityContext
	 *            the security context to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setSecurityContext(final SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

}
