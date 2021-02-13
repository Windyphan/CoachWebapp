/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author LAPTOPVTC.VN
 */

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.DBHandler.DBBean;
import model.pojo.Client;
import model.pojo.Employee;
import model.pojo.Operation;

/**
 *
 * @author WIN 10
 */
public class OperationDAO extends DAO {

    public OperationDAO(Connection con) {
        super(con);
    }

    private DBBean db = this.getDb();
    private Connection con = this.getCon();

    public ArrayList<Operation> getAllSchedule() {
//        String[][] result = db.getAllRecords("schedule");
        String query = "SELECT * FROM APP.SCHEDULE WHERE cid IS NOT NULL";
        String[][] result = db.getRecords(query);
        ArrayList<Operation> schedule = new ArrayList<Operation>();

        EmployeeDAO employeeDao = new EmployeeDAO(con);
        ClientDAO clientDao = new ClientDAO(con);

        for (String[] res : result) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));

            Client client = clientDao.getClientById(Integer.parseInt(res[2]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setClient(client);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;
    }

    // add new schedule
    public boolean addAppointment(Operation operation) {
        boolean res = false;

        // insert schedule info to 'schedule' table
        String query = String.format("INSERT INTO app.schedule(EID, CID, STYPE, NSLOT, SDATE, STIME, CANCELLED, DESCRIPTION) "
                + "VALUES (%s, %s, '%s', %s, '%s', '%s', '%s', '%s')", operation.getEmployee().getId(), operation.getClient().getId(), 
                operation.getType(), operation.getnSlot(), operation.getDate(), operation.getTime(), operation.isIsCancelled(), operation.getDescription());
        res = db.executeUpdate(query);
        if (res) {
            double charge = operation.getnSlot() * operation.getEmployee().getRate();

            String query2 = String.format("SELECT sid FROM APP.SCHEDULE WHERE"
                    + " eid=%s AND cid=%s AND sdate='%s' AND stime='%s'",
                    operation.getEmployee().getId(), operation.getClient().getId(), operation.getDate(), operation.getTime());
            String sid = db.getRecords(query2)[0][0];

            BillingDAO billingDao = new BillingDAO(con);
            res = billingDao.insertBilling(Integer.parseInt(sid), (float) Math.round((charge * 100.0) / 100.0));
        }
        return res;
    }

    // get operations that have passed and not cancelled
    public ArrayList<Operation> getAllSchedulePassedNotCancelled() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String today = simpleDateFormat.format(new Date());  // today

        String query = "SELECT * FROM APP.SCHEDULE WHERE sdate<='" + today + "' AND cancelled=false AND cid IS NOT NULL";
        String[][] results = db.getRecords(query);

        ArrayList<Operation> schedule = new ArrayList<Operation>();

        EmployeeDAO employeeDao = new EmployeeDAO(con);
        ClientDAO clientDao = new ClientDAO(con);

        for (String[] res : results) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));
            Client client = clientDao.getClientById(Integer.parseInt(res[2]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setClient(client);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;
    }

    public ArrayList<Operation> getAllScheduleFromTo(String datefrom, String dateto, String timefrom, int cid) {

        String query = "SELECT * FROM APP.SCHEDULE WHERE SDATE >='" + datefrom + "' AND SDATE <='" + dateto + "' AND cid IS NOT NULL";
        if (!timefrom.equals("") && cid != 0) {
            query = "SELECT * FROM APP.SCHEDULE WHERE SDATE >='" + datefrom
                    + "' AND SDATE <='" + dateto + "' AND STIME >= '" + timefrom + "' AND cid=" + cid;
        }
        String[][] result = db.getRecords(query);
        ArrayList<Operation> schedule = new ArrayList<Operation>();

        EmployeeDAO employeeDao = new EmployeeDAO(con);
        ClientDAO clientDao = new ClientDAO(con);

        for (String[] res : result) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));
            Client client = clientDao.getClientById(Integer.parseInt(res[2]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setClient(client);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;

    }

    public ArrayList<Operation> getScheduleByEmpId(int empId) {
        ArrayList<Operation> schedule = new ArrayList<Operation>();

        String query = "SELECT * FROM APP.SCHEDULE WHERE eid=" + empId + " AND cid IS NOT NULL ";
        String[][] result = db.getRecords(query);

        EmployeeDAO employeeDao = new EmployeeDAO(con);
        ClientDAO clientDao = new ClientDAO(con);

        for (String[] res : result) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));
            Client client = clientDao.getClientById(Integer.parseInt(res[2]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setClient(client);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;
    }

    public ArrayList<Operation> getScheduleById(int scheId) {
        ArrayList<Operation> schedule = new ArrayList<Operation>();

        String query = "SELECT * FROM APP.SCHEDULE WHERE sid=" + scheId;
        String[][] result = db.getRecords(query);

        EmployeeDAO employeeDao = new EmployeeDAO(con);
        ClientDAO clientDao = new ClientDAO(con);

        for (String[] res : result) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));
            Client client = clientDao.getClientById(Integer.parseInt(res[2]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setClient(client);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;
    }

    public ArrayList<Operation> getScheduleByCliId(int cliId) {
        ArrayList<Operation> schedule = new ArrayList<Operation>();

        String query = "SELECT * FROM APP.SCHEDULE WHERE cid=" + cliId + " AND cid IS NOT NULL";
        String[][] result = db.getRecords(query);

        EmployeeDAO employeeDao = new EmployeeDAO(con);

        for (String[] res : result) {
            Operation op = new Operation();
            Employee emp = employeeDao.getEmpById(Integer.parseInt(res[1]));

            op.setId(Integer.parseInt(res[0]));
            op.setEmployee(emp);
            op.setType(res[3]);
            op.setnSlot(Integer.parseInt(res[4]));
            op.setDate(res[5]);
            op.setTime(res[6]);
            op.setIsCancelled(Boolean.parseBoolean(res[7]));
            op.setDescription(res[8]);

            schedule.add(op);
        }

        return schedule;
    }

    
    public boolean cancelSchedule(String sid) {
        String query = "UPDATE APP.SCHEDULE SET cancelled='true' WHERE sid=" + sid;
        return db.executeUpdate(query);
    }

    
}

