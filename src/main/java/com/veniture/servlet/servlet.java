package com.veniture.servlet;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.jira.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Scanned
public class servlet extends HttpServlet {
    @JiraImport
    public IssueManager issueManager;
    @JiraImport
    private ProjectService projectService;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private ConstantsManager constantsManager;
    @JiraImport
    public RequestFactory requestFactory;
    private String action;

    private static final String LIST_ISSUES_TEMPLATE = "/templates/frontend.vm";
    public static final Logger logger = LoggerFactory.getLogger(servlet.class);

    public servlet(IssueManager issueManager,
                   SearchService searchService,
                   TemplateRenderer templateRenderer,
                   JiraAuthenticationContext authenticationContext,
                   ConstantsManager constantsManager,
                   RequestFactory requestFactory,
                   ProjectService projectService) {
        this.issueManager = issueManager;
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
        this.templateRenderer = templateRenderer;
        this.constantsManager = constantsManager;
        this.requestFactory = requestFactory;
        this.projectService = projectService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // For the performance, we are using string builder here .
        StringBuilder sb = new StringBuilder();
        Map<String, Object> context = new HashMap<>();

        // For all projects that are available/visible to the logged in user,
        for (Project project : projectService.getAllProjects(authenticationContext.getLoggedInUser()).get()) {
            // Get description or provide default value if it is "empty"
            String description = project.getDescription();
            if (description == null || description.trim().length() == 0) {
                description = "<i>No description is provided</i>";
            }

            // The document has <ul>$projects</ul> for the list, so wrapping the content in <li>
            sb.append("<li>")
                    .append(project.getName())
                    .append(" - ")
                    .append(description)
                    .append("</li>");
        }

        context.put("projects", sb.toString());
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
    }
}
