package com.adTracker.assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Goal
 */
@WebServlet("/goal")
public class Goal extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	public static String DEVICE_PARAMETER ="d1";
	public static String APP_PARAMETER ="appId";
	public static String GOAL_PARAMETER ="goal";
	public static String TIMESTAMP_PARAMETER ="ts";

	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/adtracker";
	public static final String USER = "root";
	public static final String PASSWORD = "root";

	public static final int mw=5;
	public static final int dw=6;
	Connection connection1,connection2,connection3;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	{
		String campaign="",adGroup="",clickTimeStamp="";
		PrintWriter out = response.getWriter();
		out.println("GoalEvent Recorded");
		//System.out.println("Got goal");
		try
		{
			Class.forName(DRIVER_CLASS);
			connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
		}
		catch ( ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		Map<String, String[]> parameterMap = request.getParameterMap();
		String deviceId = parameterMap.get(DEVICE_PARAMETER)[0];
		String appId = parameterMap.get(APP_PARAMETER)[0];
		String goalName= parameterMap.get(GOAL_PARAMETER)[0];
		String timestamp = parameterMap.get(TIMESTAMP_PARAMETER)[0];
		
		//System.out.println( DEVICE_PARAMETER + ": " + deviceId);
		//System.out.println( APP_PARAMETER + ": " + appId);
		//System.out.println( GOAL_PARAMETER + ": " + goalName);
		//System.out.println( TIMESTAMP_PARAMETER + ": " + timestampFormatter(timestamp));
		
		long milliSeconds= Long.parseLong(timestamp);
		long matchingMilliSeconds= milliSeconds - TimeUnit.MILLISECONDS.convert(mw, TimeUnit.DAYS);
		String matchingTimestamp = String.valueOf(matchingMilliSeconds);
		String matchingWindowQuery = "SELECT `adg`,`cmp`,`ts` FROM `adtracker`.`click` WHERE `d1` = ? AND `appId` = ? AND ts > ? AND ts < ? ORDER BY `ts` DESC;";
		try
		{
			PreparedStatement matchingWindowStatement = connection1.prepareStatement(matchingWindowQuery);
			matchingWindowStatement.setString(1, deviceId);
			matchingWindowStatement.setString(2, appId);
			matchingWindowStatement.setString(3, matchingTimestamp);
			matchingWindowStatement.setString(4, timestamp);
			ResultSet resultMatched = matchingWindowStatement.executeQuery();
			try
			{
				if(resultMatched.next())
				{
					campaign = resultMatched.getString("cmp");
					clickTimeStamp = resultMatched.getString("ts");
					adGroup = resultMatched.getString("adg");
					//System.out.println(campaign);
					//System.out.println(clickTimeStamp);
					//System.out.println(adGroup);
					connection1.close();
				}
				else
				{
					connection1.close();
					return;
				}
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		long dedupingMilliSeconds= milliSeconds - TimeUnit.MILLISECONDS.convert(dw, TimeUnit.DAYS);
		String dedupingTimestamp = String.valueOf(dedupingMilliSeconds);
		String dedupingWindowQuery = "SELECT COUNT(*) AS `count` FROM `adtracker`.`goals_converted` WHERE `d1` = ? AND `appId` = ? AND `goal` = ? AND `ts` > ? AND `ts` < ?;";
		try
		{
			connection2 = DriverManager.getConnection(URL, USER, PASSWORD);
			
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			PreparedStatement dedupingWindowStatement = connection2.prepareStatement(dedupingWindowQuery);
			dedupingWindowStatement.setString(1, deviceId);
			dedupingWindowStatement.setString(2, appId);
			dedupingWindowStatement.setString(3, goalName);
			dedupingWindowStatement.setString(4, dedupingTimestamp);
			dedupingWindowStatement.setString(5, timestamp);
			ResultSet count = dedupingWindowStatement.executeQuery();
			if(count.next())
			{
				//System.out.println(count.getInt(1));
				if(count.getInt(1)==0)
				{
					connection2.close();
					try
					{
						connection3 = DriverManager.getConnection(URL, USER, PASSWORD);
					}
					catch (SQLException e1)
					{
						e1.printStackTrace();
					}
					String insertQuery = "INSERT INTO `adtracker`.`goals_converted` (`d1`,`appId`,`goal`,`ts`,`adg`,`cmp`,`click_ts`) VALUES (?,?,?,?,?,?,?);";
					try
					{
						PreparedStatement insertStatement = connection3.prepareStatement(insertQuery);
						insertStatement.setString(1, deviceId);
						insertStatement.setString(2, appId);
						insertStatement.setString(3, goalName);
						insertStatement.setString(4, timestamp);
						insertStatement.setString(5, adGroup);
						insertStatement.setString(6, campaign);
						insertStatement.setString(7, timestampFormatter(clickTimeStamp));
						insertStatement.executeUpdate();
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
					connection3.close();
				}
				else
				{
					connection2.close();
					return;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public String timestampFormatter(String timestamp)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long milliSeconds= Long.parseLong(timestamp);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}
