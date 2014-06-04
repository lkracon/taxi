package com.taxi.controller.rest;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taxi.exception.GeneralException;
import com.taxi.exception.UserException;
import com.taxi.model.AccessToken;
import com.taxi.model.User;
import com.taxi.service.TaxiService;
import com.taxi.service.UserService;

@Transactional
@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaxiService taxiService;

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public User getUser(@PathVariable Long id) throws UserException {
        return userService.getUser(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST , consumes = "application/json", produces = "application/json")
    @ResponseBody
    public User updateUser(@PathVariable Long id, @RequestBody User user) throws MissingServletRequestParameterException {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public User addUser(@RequestBody User user) throws GeneralException, MissingServletRequestParameterException {
        if (user.getId() != null) {
            throw new GeneralException("field id not allowed", GeneralException.GENERAL_EXCEPTION);
        }
        if (user.getLogin() == null) {
            throw new MissingServletRequestParameterException("login", "String");
        }
        if (user.getPassword() == null) {
            throw new MissingServletRequestParameterException("password", "String");
        }
        user.setCreationDate(Calendar.getInstance().getTime());
        user.setModificationDate(Calendar.getInstance().getTime());
        return userService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public User deleteUser(@PathVariable Long id) {

        return userService.delete(id);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public AccessToken authenticate(@RequestParam String login, @RequestParam String password) throws UserException {
        return userService.authenticate(login, password);
    }

}
