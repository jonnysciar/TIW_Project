package it.jonnysciar.javascript.servlets;

import com.google.gson.Gson;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/getHomepage")
public class GetHomepage extends DBServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        Utente utente = (Utente) request.getSession().getAttribute("user");
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);

        List<Prodotto> prodotti;
        List<Preventivo> preventivi;
        try {
            prodotti = prodottoDAO.getAll();
            preventivi = preventivoDAO.getAllByUserId(utente.getId());

            for(Prodotto p : prodotti) {
                p.setOpzioni(prodottoDAO.getAllOpzioniById(p.getId()));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Qualcosa è andato storto!");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(List.of(prodotti, preventivi)));
    }

}
