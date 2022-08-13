package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/CheckPrice")
@MultipartConfig
public class ControlloPrezzo extends DBServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
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
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Errore nella richiesta!");
            return;
        }

        if (prezzo <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Il prezzo deve essere maggiore di 0!");
        } else {

            try {
                preventivoDAO.updatePreventivoById(preventivo.getId(), utente.getId(), prezzo);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Qualcosa è andato storto!");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
