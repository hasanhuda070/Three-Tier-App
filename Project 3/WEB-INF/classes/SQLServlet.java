/*	Name: Showad Huda
	Course: CNT 4714 – Summer 2021 – Project Three
	Assignment title: A Three-Tier Distributed Web-Based Application
	Date: August 5, 2021
*/

//Pre-porcessor directives
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLServlet extends HttpServlet {
	private Connection connection;
	private Statement statement;

	//Database connection
	@Override
	public void init(ServletConfig config) throws ServletException {
		// @Override
		super.init(config);
		// connected to database sql
		try {
			//XML
			Class.forName(config.getInitParameter("databaseDriver"));
			connection = DriverManager.getConnection(config.getInitParameter("databaseName"),
					config.getInitParameter("username"), config.getInitParameter("password"));
			statement = connection.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new UnavailableException(e.getMessage());
		}
	}

	//Get jsp with input and output
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String inputBox = request.getParameter("inputBox");
		String inputBoxLowerCase = inputBox.toLowerCase();
		String outputs = null;
		
		//select sql statement test
		if (inputBoxLowerCase.contains("select")) {

			try {
				outputs = doSelectQuery(inputBoxLowerCase);
			} catch (SQLException e) {
				outputs = "<span>" + e.getMessage() + "</span>";
				e.printStackTrace();
			}
		}
		else { //Other sql statement tests
			try {
				outputs = doUpdateQuery(inputBoxLowerCase);
			}catch(SQLException e) {
				outputs = "<span>" + e.getMessage() + "</span>";
				e.printStackTrace();
			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("outputs", outputs);
		session.setAttribute("inputBox", inputBox);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

	//Post jsp form
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	//Table creation from input
	public String doSelectQuery(String inputBox) throws SQLException {
		String outputs;
		ResultSet table = statement.executeQuery(inputBox);
		ResultSetMetaData metaData = table.getMetaData();
		int numOfColumns = metaData.getColumnCount();
		String tableOpeningHTML = "<div class='container-fluid'><div class='row justify-content-center'><div class='table-responsive-sm-12 table-responsive-md-12 table-responsive-lg-12'><table class='table'>";
		String tableColumnsHTML = "<thead class='thead-light'><tr>";
		for (int i = 1; i <= numOfColumns; i++) {
			tableColumnsHTML += "<th scope='col'>" + metaData.getColumnName(i) + "</th>";
		}
		tableColumnsHTML += "</tr></thead>";
		String tableBodyHTML = "<tbody>";
		while (table.next()) {
			tableBodyHTML += "<tr>";
			for (int i = 1; i <= numOfColumns; i++) {
				if (i == 1)
					tableBodyHTML += "<td scope'row'>" + table.getString(i) + "</th>";
				else
					tableBodyHTML += "<td>" + table.getString(i) + "</th>";
			}
			tableBodyHTML += "</tr>";
		}
		tableBodyHTML += "</tbody>";
		String tableClosingHTML = "</table></div></div></div>";
		outputs = tableOpeningHTML + tableColumnsHTML + tableBodyHTML + tableClosingHTML;
		return outputs;
	}
	private String doUpdateQuery(String inputBoxLowerCase) throws SQLException {
		String outputs = null;
		int numOfRowsUpdated = 0;
		
		//Bonus question set up
		ResultSet beforeQuantityCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		beforeQuantityCheck.next();
		int numOfShipmentsGreaterThanBefore = beforeQuantityCheck.getInt(1);
		statement.executeUpdate("create table shipmentsBeforeUpdate like shipments");
		statement.executeUpdate("insert into shipmentsBeforeUpdate select * from shipments");
		numOfRowsUpdated = statement.executeUpdate(inputBoxLowerCase);
		outputs = "<div>Command Executed Successfully.</div><div>" + numOfRowsUpdated + " row(s) affected</div>";

		ResultSet afterQuantityCheck = statement.executeQuery("select COUNT(*) from shipments where quantity >= 100");
		afterQuantityCheck.next();
		int numOfShipmentsGreaterThanAfter = afterQuantityCheck.getInt(1);
		outputs += "<div>" + numOfShipmentsGreaterThanBefore + " < " + numOfShipmentsGreaterThanAfter + "</div>";
		
		if(numOfShipmentsGreaterThanBefore < numOfShipmentsGreaterThanAfter) {
			//increments by 5
			int numberOfRows5 = statement.executeUpdate("Update Status = status + 5 where snum in ( select distinct snum from shipments left join shipmentsBeforeUpdate using (snum, pnum, jnum, quantity) where shipmentsBeforeUpdate.snum is null)");
			outputs += "<div>Business Logic Detected! - Updating Supplier Status</div>";
			outputs += "<div>Business Logic Updated " + numberOfRows5 + " Supplier(s) status marks</div>";
			outputs += "<div>Business Logic Not Triggered! - Updating Supplier Status</div>";
		}

		statement.executeUpdate("drop table shipmentsBeforeUpdate");
		return outputs;
	}
	
	 public static void SQLServlet(String[] args) {
     JOptionPane.showMessageDialog(null, "Error - SQLException",
      "Error", JOptionPane.ERROR_MESSAGE);

  }

}