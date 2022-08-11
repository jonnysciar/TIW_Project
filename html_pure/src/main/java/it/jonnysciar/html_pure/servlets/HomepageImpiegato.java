package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepageImpiegato")
public class HomepageImpiegato extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        Utente utente = (Utente) request.getSession().getAttribute("user");
        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        List<Preventivo> prezzati;
        List<Preventivo> daPrezzare;
        try {
            prezzati = preventivoDAO.getAllByImpiegatoId(utente.getId());
            daPrezzare = preventivoDAO.getAllPreventiviNotManaged();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
            return;
        }
        ctx.setVariable("prezzati", prezzati);
        ctx.setVariable("daPrezzare", daPrezzare);
        ctx.setVariable("name", utente.getNome());

        String path = "/templates/homepageImpiegato.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

}
