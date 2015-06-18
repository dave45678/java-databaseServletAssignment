

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetCustomerInvoice
 */
@WebServlet("/GetCustomerInvoice")
public class GetCustomerInvoice extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String connectionUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCustomerInvoice() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String custId = request.getParameter("customerID");
		try {
			ResultSet custInfo = getCustomerInfo(custId);
			ResultSet invoiceInfo = getInvoices(custId);
			
			out.println("Customer Invoice for customer with id:" + custId + "<br />");
			if (custInfo.next())
			{
				out.println("<br />Customer Name: " + custInfo.getString("cust_last_name") + " " + custInfo.getString("cust_last_name"));
			} else
			{
				out.println("<br />This customer does seem to exist");
			}
			
			out.println("<br /><br />");
			
			out.println("<table>");
			out.println("<tr><th>Customer ID</th><th>Order ID</th><th>Order Total</th></tr>");
			while (invoiceInfo.next())
			{
				out.println("<tr><td>$"+ invoiceInfo.getString("order_total") +"</td><td>"+ invoiceInfo.getString("order_id") +"</td><td>" + custId + "</td></tr>");
			}
		} catch (Exception e){
			out.println("<h2>Error! Danger Will Robinson Danger!</h2>"+e);
		}finally
		{
			out.close();
		}
	}
	
	protected ResultSet getCustomerInfo(String custId)
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		
		    Connection conn =
		            DriverManager.getConnection(connectionUrl,"system","passwordy");

	       conn.setAutoCommit(false);
	       Statement stmt = conn.createStatement();
	       return stmt.executeQuery("Select cust_first_name, cust_last_name from testdb.demo_customers where customer_id = " + custId);
		} catch (Exception e){
			return null;
		} 
	}
	
	protected ResultSet getInvoices(String custId)
	{
		try
		{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
	    Connection conn =
	            DriverManager.getConnection(connectionUrl,"system","passwordz");

	       conn.setAutoCommit(false);
	       Statement stmt = conn.createStatement();
	       return stmt.executeQuery("select order_id, order_total from testdb.demo_orders where customer_id = " + custId);
		} catch (Exception e){
			return null;
		
		}
	}

}
