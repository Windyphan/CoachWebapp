/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.clients;

/**
 *
 * @author LAPTOPVTC.VN
 */

import dao.ClientDAO;
import dao.EmployeeDAO;
import dao.OperationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.pojo.Client;
import model.pojo.Employee;
import model.pojo.Operation;


@WebServlet(name = "Book_Appointment", urlPatterns = {"/Book_Appointment"})
public class Book_Appointment extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(); // set session

            //connect DAO
            Connection con = (Connection) getServletContext().getAttribute("con");
            ClientDAO clientDao = new ClientDAO(con);
            EmployeeDAO employeeDao = new EmployeeDAO(con);
            OperationDAO operationDao = new OperationDAO(con);

            ArrayList<Employee> staffsList = employeeDao.getAllEmployees();
            session.setAttribute("staffs", staffsList);

            // load data for the first time
            if (request.getParameter("booking-submit") == null) {
                request.getRequestDispatcher("/viewer/client/Book_Appointment.jsp").forward(request, response);
            } else {
                //get input data
                String staffid = request.getParameter("staff-required").trim();
                String slot = request.getParameter("slot").trim();
                String description = request.getParameter("description").trim();
                String dateString = request.getParameter("booking-date").trim();
                String timeString = request.getParameter("booking-time").trim();

                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                String bookDateStr = dateString + "T" + timeString + ":00"; // "yyyy-MM-ddTHH:mm:ss";
                Date bookDate = formatter.parse(bookDateStr);
                
                // compareTo returns -1/0/1 if less/equal/greater
                if (bookDate.compareTo(today) != 1) {
                    request.setAttribute("date-select-error", "wrong");
                    request.getRequestDispatcher("/viewer/client/Book_Appointment.jsp").forward(request, response);
                }
                else {
                    //get require staff data
                    Employee requireEmployee = employeeDao.getEmpById(Integer.parseInt(staffid));
                    //get client data
                    Client client = clientDao.getClientData((String) session.getAttribute("fullName"));

                    Operation operation = new Operation();
                    operation.setEmployee(requireEmployee);
                    operation.setClient(client);
                    operation.setType("appointment");
                    operation.setnSlot(Integer.parseInt(slot));
                    operation.setDate(dateString);
                    operation.setTime(timeString);
                    operation.setIsCancelled(false);
                    operation.setDescription(description);

                    boolean res = operationDao.addAppointment(operation);
                    if (res) {
                        response.sendRedirect("/viewer/client/Manage_Appointment.jsp");
                    } else {
                        out.print("<small class=\"Error Error-Booking\">There's been some error. Please try again later.</small>");
                        request.getRequestDispatcher("/viewer/client/Book_Appointment.jsp").include(request, response);
                    }
                }

                
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(Book_Appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(Book_Appointment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

