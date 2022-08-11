package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/prezzaPreventivo")
public class PrezzaPreventivo extends ThymeLeafServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "/WEB-INF/templates/prezzaPreventivo.html";
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        Prodotto prodotto;
        List<Opzione> opzioni;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("id")));
            if (preventivo == null || preventivo.getPrezzo() != 0 || preventivo.getId_impiegato() != 0) {
                throw new SQLException();
            }
            opzioni = preventivoDAO.getAllOpzioniById(preventivo.getId());
            prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
        } catch (SQLException | NumberFormatException e) {
            ctx.setVariable("good", false);
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        ctx.setVariable("good", true);
        ctx.setVariable("opzioni", opzioni);
        ctx.setVariable("preventivo", preventivo);
        ctx.setVariable("prodotto", prodotto);
        ctx.setVariable("preventivoId", preventivo.getId());
        templateEngine.process(path, ctx, response.getWriter());
    }
}
