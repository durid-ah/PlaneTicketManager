import com.planeTicketManager.helpers.PropertyReader;
import com.planeTicketManager.models.FlightCriteria;
import com.planeTicketManager.repository.FlightRepo;
import com.planeTicketManager.repository.UserRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

public class TestingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        FlightCriteria criteria = new FlightCriteria();

        criteria.setArrivalAirport("O'Hare International Airport");
        //criteria.setDepartureAirport("");
        criteria.setArrivalTime(new Date(2019, 3,14));
        criteria.setDepartureTime(new Date(2019, 3, 14));

        FlightRepo repo = new FlightRepo();
        UserRepo userRepo = new UserRepo();

        try {
            out.println(userRepo.getUserFlights(1));
        } catch (SQLException e) {
            out.println( e.getMessage());
        }
    }
}
