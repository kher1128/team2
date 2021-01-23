package com.youngwoo.teamtwo;


import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MariaController {
    @RequestMapping(value = "/getData", method= RequestMethod.GET)
    @ResponseBody() // JSON
    public Map  home(@RequestParam(value="company_id", required=false) String company_id) {
        Map result = new HashMap<String, Object>();
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://team2.cqwr50ybrtaw.ap-northeast-2.rds.amazonaws.com/", "team2", "team2!!!!")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("USE team2")) { // 데이터베이스 선택
                    String SQL = "select * from company_info where company_id = \'"  + company_id + "\'";
                    ResultSet rs2 = stmt.executeQuery(SQL); // 삼성전자 가격 조회
                    while(rs2.next()){
                        String ret1 = rs2.getString(1);
                        String ret2 = rs2.getString(2);
                        result.put(ret1, ret2);
                    }
                } catch(Exception e){
                    System.out.println(e);
                }
            } catch(Exception e){
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return result;
    }

    @RequestMapping(value = "/kosdaq", method= RequestMethod.GET)
    @ResponseBody() // JSON
    public Map kosdaq_problem() {
        Map result = new HashMap<String, Object>();
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://team2.cqwr50ybrtaw.ap-northeast-2.rds.amazonaws.com/", "team2", "team2!!!!")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("USE team2")) {
                    String SQL = "select *\n" +
                            "from kosdaq_info";
                    ResultSet kosdaqQuery = stmt.executeQuery(SQL); // 삼성전자 가격 조회
                    while(kosdaqQuery.next()){
                        result.put("kosdaq_start", kosdaqQuery.getString(1));
                        result.put("kosdaq_end", kosdaqQuery.getString(1));
                        result.put("kosdaq_min", kosdaqQuery.getString(1));
                        result.put("kosdaq_max", kosdaqQuery.getString(1));
                    }
                } catch(Exception e){
                    System.out.println(e);
                }
            } catch(Exception e){
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return result;
    }

    @RequestMapping(value = "/companyList", method= RequestMethod.GET)
    @ResponseBody() // JSON
    public List getCompanyList() {
        List list = new ArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://team2.cqwr50ybrtaw.ap-northeast-2.rds.amazonaws.com/", "team2", "team2!!!!")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("USE team2")) {
                    String SQL = "select *\n" +
                            "from company_info;";
                    ResultSet queryResult = stmt.executeQuery(SQL);
                    while(queryResult.next()){
                        Map result = new HashMap<String, Object>();
                        result.put("company_name", queryResult.getString(1));
                        result.put("company_id", queryResult.getString(2));
                        list.add(result);
                    }
                } catch(Exception e){
                    System.out.println(e);
                }
            } catch(Exception e){
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }
}