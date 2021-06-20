/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    String name = "Bobby";
    model.put("name", name);
    return "index"; // rename this to whatever main .html file is
  }

  @GetMapping(
    path = "/rectangle"
  )
  public String getRectangleForm(Map<String, Object> model) {
    Rectangle rectangle = new Rectangle(); // creates new rectangle object with empty attributes
    model.put("rectangle", rectangle);
    return "rectangle";
  }

  @PostMapping(
    path = "/rectangle",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserRectangleSubmit(Map<String, Object> model, Rectangle rectangle) throws Exception {
    // save rectangle data into db
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Rectangle (id serial, name varchar(20), width varchar(20), height varchar(20), colour varchar(20))");
      String sql = "INSERT INTO Rectangle (name,width,height,colour) VALUES ('" + rectangle.getName() + "','" + rectangle.getWidth() + "','" + rectangle.getHeight() + "','" + rectangle.getColour() + "')";
      stmt.executeUpdate(sql);
      System.out.println(rectangle.getName() + " " + rectangle.getColour()); // print rectangle on console
      return "redirect:/rectangle/success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/rectangle/success")
  public String getRectangleSuccess(Map<String, Object> model) {
   try (Connection connection = dataSource.getConnection()) {
     Statement stmt = connection.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM Rectangle");

     ArrayList<String> output = new ArrayList<String>();    // this part needs to change (see discord)
     while (rs.next()) {
       String name = rs.getString("name");
       String id = rs.getString("id");

       output.add(id + "," + name);
     }

     model.put("records", output);
     return "success";
   } catch (Exception e) {
     model.put("message", e.getMessage());
     return "error";
   }
  }

  @GetMapping("/rectangle/read/{pid}")
  public String getSpecificRectangle(Map<String, Object> model, @PathVariable String pid) {
    System.out.println(pid);
    //
    //query DB : SELECT * FROM Rectangle WHERE id={pid}
    model.put("id", pid);
    return "readrectangle";
  }

  @GetMapping("/rectangle/read")
  public String getSpecificRectangle2(Map<String, Object> model, @RequestParam String pid) {
    System.out.println(pid);
    //
    // query DB : SELECT * FROM people WHERE id={pid}
    model.put("id", pid);
    return "readrectangle";
  }

  @ResponseBody
  @DeleteMapping("/rectangle/{pid}")
  public String DeleteRectangle(@PathVariable String pid) {
    // delete rectangle with pid
    System.out.println(pid);
    return "deletesuccess";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
