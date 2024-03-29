package it.jonnysciar.javascript.servlets;

import com.google.gson.Gson;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;

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
        Utente utente = (Utente) request.getSession().getAttribute("user");

        if (utente.isImpiegato()) getHomepageImpiegato(utente, response);
        else getHomepageUtente(utente, response);
    }

    private void getHomepageUtente(Utente utente, HttpServletResponse response) throws IOException {
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
        response.getWriter().println(new Gson().toJson(List.of(prodotti, preventivi)));
    }

    private void getHomepageImpiegato(Utente utente, HttpServletResponse response) throws IOException {

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);

        List<Preventivo> preventiviGestiti;
        List<Preventivo> preventiviDaGestire;
        try {
            preventiviGestiti = preventivoDAO.getAllByImpiegatoId(utente.getId());
            preventiviDaGestire = preventivoDAO.getAllPreventiviNotManaged();

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Qualcosa è andato storto!");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(new Gson().toJson(List.of(preventiviGestiti, preventiviDaGestire)));
    }

}
