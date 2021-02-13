<%-- 
    Document   : Book_Appointment
    Created on : Feb 13, 2021, 3:16:25 AM
    Author     : LAPTOPVTC.VN
--%>

<!DOCTYPE html>
<jsp:include page="/viewer/Header.jsp"/>

<div class="MainContent">

    <h3>Let's Make A Booking!!</h3>
    
    <%
        if (request.getAttribute("date-select-error") != null) {
            out.print("<h3 style=\"color: red\">Chosen dates not valid. Try again.</h3>");
        }
        %>
    
    <form action="/Book_Appointment" method="post" class="FormTable">
        <table id="book-appointment">
            <tr>
                <th>Staff require <br>
                    
                    <select name="staff-required" required>
                <option value=null>Select a staff.......</option>
                <%
                    for (Employee emp : staffs) {
                        out.print("<option value=\"" + emp.getId()+ "\">" + emp.getFullName() + "</option>");
                    }
                %>
            </select>
                
                    
                </th>
                <th>Date
                    <input required type="date" class="form-control" name="booking-date" placeholder="yyyy-MM-dd" title="yyyy-MM-dd"/>
                </th>
                <th>Time <br>
                    <input required type="time" class="form-control" name="booking-time" placeholder="HH:mm:ss" title="HH:mm:ss"/>
                </th>
                <th>Number of slot(s)
                    <input required type="number" class="form-control" name="slot" min="1" max="6"/>
                </th>
                <th>Decription
                    <textarea required class="form-control" name="description"></textarea>
                </th>
            </tr>
          
        </table>
        <input type="submit" name="booking-submit" value="Book"/>
    </form>
            
</div>
<jsp:include page="/viewer/Footer.jsp"/>

