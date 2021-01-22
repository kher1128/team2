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
        String ret = "";
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://team2.cqwr50ybrtaw.ap-northeast-2.rds.amazonaws.com/", "team2", "team2!!!!")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("SHOW Databases;")) {
                    stmt.executeQuery("USE team2");   // 데이터베이스 선택
//                    ResultSet rs1 = stmt.executeQuery("SHOW Tables"); // 테이블 목록 조회
//                    while(rs1.next()){
//                        String sang2 = rs1.getString(1);
//                        System.out.println(sang2);
//                    }

                    String SQL = "select * from company_price_info where company_id = \'"  + company_id + "\'";
                    ResultSet rs2 = stmt.executeQuery(SQL); // 삼성전자 가격 조회
//                    ResultSet rs2 = stmt.executeQuery("SHOW COLUMNS FROM company_price_info;"); // 테이블 컬럼 조회
                    while(rs2.next()){
                        String ret1 = rs2.getString(1);
                        String ret2 = rs2.getString(2);
                        ret += ret1 + " " + ret2 + "\n";
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
}