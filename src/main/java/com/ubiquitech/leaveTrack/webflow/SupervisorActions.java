package com.ubiquitech.leaveTrack.webflow;

import com.ubiquitech.leaveTrack.domain.Employee;
import com.ubiquitech.leaveTrack.domain.Request;
import com.ubiquitech.leaveTrack.eMail.Mail;
import com.ubiquitech.leaveTrack.form.ProcessEmployeeLeaveForm;
import com.ubiquitech.leaveTrack.services.EmployeeService;
import com.ubiquitech.leaveTrack.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.core.collection.SharedAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vane on 2014/12/11.
 */
public class SupervisorActions extends MultiAction {
    private RequestService requestService;
    private EmployeeService employeeService;
    private  List<Request> requestsInLoggedStatus;
    private   List<Request>  requestSelected;
    @Autowired
    private Mail mail;
    final Logger logger = LoggerFactory.getLogger(RequestLeaveActions.class);

    public Event getLoggedRequests(RequestContext context,SharedAttributeMap map){
        Employee employee =(Employee)map.get("employeeSession");
        System.out.println("Employee ID:"+employee.getId());
        requestsInLoggedStatus =  requestService.getRequestsByStatusAndSupervisorId("Logged",employee.getId());
        context.getFlowScope().put("requestsLogged", requestsInLoggedStatus);
        return success();
    }

    public Event selectLeaveRequest(int requestId,ProcessEmployeeLeaveForm form){

        requestSelected =  requestService.getRequestsByStatusAndRequestId("Logged",(long)requestId);
        form.setRequest(requestSelected.get(0));
        //setting next state options
        List<String> nextState = new ArrayList<String>();
        nextState.add("Approved");
        nextState.add("Rejected");
        form.setMap(nextState);
        form.setEmployeeFullName(form.getRequest().getEmployee().getFirstName()+" "+ form.getRequest().getEmployee().getLastName());
        return success();
    }

    public Event updateRequest(ProcessEmployeeLeaveForm form) {
        Request request = form.getRequest();
        requestService.createRequest(request);
        return success();
    }

    public Event sendEmplyeeEmail(ProcessEmployeeLeaveForm form,SharedAttributeMap map) {
        Employee employee = (Employee) map.get("employeeSession");
        String employeeEmail=form.getRequest().getEmployee().getEmail();
        System.out.println("Employee Email: "+ employeeEmail );
        try {
            mail.sendMail(
                /*FROM:*/ employee.getEmail(),
                  /*TO:*/ employeeEmail,
             /*SUBJECT:*/  "Leave Request",
             /*MESSAGE:*/  "==========This is an automatically generated Email, Please do not reply.==========\n" + employee.getFirstName() + " " + employee.getLastName()
                    + " has "+form.getRequest().getState()+" you leave request, Please log into LeaveTrack for more details");
            return success();
        } catch (Exception e) {
            logger.error("Email could not be sent");
            logger.debug("Debug message", e);
            return error();
        }
    }


    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }
}
