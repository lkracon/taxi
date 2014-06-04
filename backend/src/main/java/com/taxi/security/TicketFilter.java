package com.taxi.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.taxi.model.AccessToken;
import com.taxi.repository.AccessTokenRepository;

/**
 * Extracts Alfresco ticket from the request Authorization header. Ticket is in
 * the form:
 * 
 * Authorization: Bearer {ticket here}
 * 
 * @author lkracon
 * 
 */
public class TicketFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return getTicket(request);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String ticket = getTicket(request);
        AccessToken accessToken = accessTokenRepository.findByToken(ticket);
        return accessToken == null || accessToken.getExpire().getTime() < new Date().getTime() ? null : accessToken
                .getUser();
    }

    protected String getTicket(HttpServletRequest request) {
        String ticket = request.getHeader("Authorization");

        if (ticket != null) {
            ticket = ticket.substring("Bearer ".length());
        } else {
            ticket = request.getParameter("authorization");
        }

        return ticket;
    }
}
