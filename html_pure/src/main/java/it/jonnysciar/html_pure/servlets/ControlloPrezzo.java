package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CheckPrice")
public class ControlloPrezzo extends ThymeLeafServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utente utente = (Utente) request.getSession().getAttribute("user");

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        int prezzo;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("preventivoId")));
            prezzo = Integer.parseInt(request.getParameter("price"));
            if (preventivo == null || preventivo.getPrezzo() != 0 || preventivo.getId_impiegato() != 0) {
                throw new SQLException();
            }
        } catch (SQLException | NumberFormatException e) {
            setupPageError(request, response);
            return;
        }

        if (prezzo <= 0) {
            String path = "/WEB-INF/templates/prezzaPreventivo.html";
            response.setCharacterEncoding("UTF-8");
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

            Prodotto prodotto;
            List<Opzione> opzioni;
            try {
                opzioni = preventivoDAO.getAllOpzioniById(preventivo.getId());
                prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
            } catch (SQLException e) {
                setupPageError(request, response);
                return;
            }

            ctx.setVariable("good", true);
            ctx.setVariable("opzioni", opzioni);
            ctx.setVariable("preventivo", preventivo);
            ctx.setVariable("prodotto", prodotto);
            ctx.setVariable("preventivoId", preventivo.getId());
            ctx.setVariable("errorMsg", "Il prezzo deve essere maggiore di 0");
            templateEngine.process(path, ctx, response.getWriter());
        } else {
            try {
                preventivoDAO.updatePreventivoById(preventivo.getId(), utente.getId(), prezzo);
            } catch (SQLException e) {
                setupPageError(request, response);
                return;
            }
            response.sendRedirect(getServletContext().getContextPath() + "/homepageImpiegato");
        }
    }

    private void setupPageError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "/WEB-INF/templates/prezzaPreventivo.html";
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        ctx.setVariable("good", false);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
