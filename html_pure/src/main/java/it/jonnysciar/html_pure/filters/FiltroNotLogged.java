package it.jonnysciar.html_pure.filters;

import it.jonnysciar.html_pure.beans.Utente;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FiltroNotLogged implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String homepath = req.getServletContext().getContextPath();
        HttpSession session = req.getSession();

        if (!(session.isNew() || session.getAttribute("user") == null)) {
            Utente utente = (Utente) session.getAttribute("user");

            if (utente.isImpiegato()) homepath = homepath + "/homepageImpiegato";
            else homepath = homepath + "/homepageUtente";

            res.sendRedirect(homepath);
            return;
        }
        // pass the request along the filter chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
