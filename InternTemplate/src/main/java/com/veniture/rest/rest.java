package com.veniture.rest;


import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;
import com.atlassian.jira.util.json.JSONObject;

@Path("/main")
public class rest {
    @JiraImport
    private RequestFactory requestFactory;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private IssueManager issueManager;


    public rest(RequestFactory requestFactory, SearchService searchService, JiraAuthenticationContext authenticationContext){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
    }

    @GET
    @Path("/child")
    public String child(@Context HttpServletRequest req, @Context HttpServletResponse resp)  {
        String apiPath = "https://api.exchangeratesapi.io/latest?base=USD&symbols=TRY";

        try {
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(apiPath);
            client.executeMethod(method);
            String response = method.getResponseBodyAsString();
            method.releaseConnection();

            JSONObject obj = new JSONObject(response);
            double rate = obj.getJSONObject("rates").getDouble("TRY");

            return String.valueOf(rate);
        }
        catch (Exception ex) {
            return "An error occurred";
        }
    }
}
