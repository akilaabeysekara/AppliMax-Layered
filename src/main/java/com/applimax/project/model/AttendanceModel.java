package com.applimax.project.model;

import com.applimax.project.dto.AttendanceDTO;
import com.applimax.project.dto.tm.AttendanceTM;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.applimax.project.model.AppUserModel.getString;

public class AttendanceModel {
    public ArrayList<AttendanceTM> getAllAttendance() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM attendance");

        ArrayList<AttendanceTM> list = new ArrayList<>();

        while (resultSet.next()) {
            AttendanceTM attendanceTM = new AttendanceTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getInt(7)
            );

            list.add(attendanceTM);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT attendance_id FROM attendance ORDER BY attendance_id DESC LIMIT 1");
        char tableChar = 'A';
        return getString(resultSet, tableChar);
    }

    public boolean saveAttendance(AttendanceDTO attendanceDTO) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO attendance VALUES (?,?,?,?,?,?,?)",
                attendanceDTO.getAttendanceId(),
                attendanceDTO.getEmployeeId(),
                attendanceDTO.getAttendanceDate(),
                attendanceDTO.getAttendanceInTime(),
                attendanceDTO.getAttendanceOutTime(),
                attendanceDTO.getStatus(),
                attendanceDTO.getWorkingHours()
        );
    }

    public boolean updateAttendance(AttendanceDTO attendanceDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE attendance SET employee_id=?, attendance_date=?, attendance_in_time=?, attendance_out_time=?, status=?, working_hours=? WHERE attendance_id=?",
                attendanceDTO.getEmployeeId(),
                attendanceDTO.getAttendanceDate(),
                attendanceDTO.getAttendanceInTime(),
                attendanceDTO.getAttendanceOutTime(),
                attendanceDTO.getStatus(),
                attendanceDTO.getWorkingHours(),
                attendanceDTO.getAttendanceId()
        );
    }

    public boolean deleteAttendance(String attendanceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM attendance WHERE attendance_id = ?",
                attendanceId
        );
    }

    public ArrayList<String> getAllAttendanceIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT attendance_id FROM attendance"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public AttendanceDTO findById(String selectedId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM attendance WHERE attendance_id=?",
                selectedId
        );
        if (resultSet.next()) {
            return new AttendanceDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3),
                    resultSet.getTime(4),
                    resultSet.getTime(5),
                    resultSet.getString(6),
                    resultSet.getInt(7)
            );
        }
        return null;
    }
}