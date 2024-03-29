package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import it.jonnysciar.html_pure.dao.UtenteDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dettagliPreventivo")
public class DettagliPreventivo extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "/WEB-INF/templates/dettagliPreventivo.html";
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        Utente utente = (Utente) request.getSession().getAttribute("user");
        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        Prodotto prodotto;
        String impiegato = null;
        List<Opzione> opzioni;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("id")));
            if (preventivo == null || (utente.getId() != preventivo.getId_utente() && utente.getId() != preventivo.getId_impiegato())) {
                throw new SQLException();
            }
            if (preventivo.getId_impiegato() != 0) {
                impiegato = new UtenteDAO(connection).getById(preventivo.getId_impiegato()).getUsername();
            }
            opzioni = preventivoDAO.getAllOpzioniById(preventivo.getId());
            prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
        } catch (SQLException | NumberFormatException e) {
            ctx.setVariable("good", false);
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        ctx.setVariable("good", true);
        ctx.setVariable("impiegato", impiegato);
        ctx.setVariable("opzioni", opzioni);
        ctx.setVariable("preventivo", preventivo);
        ctx.setVariable("prodotto", prodotto);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
