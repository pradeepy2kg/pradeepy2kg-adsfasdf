package lk.rgd.crs.web;

import lk.rgd.Permission;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.util.CommonUtil;
import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.crs.api.service.MarriageRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;

/**
 * Validates user access, and serves scanned image of the marriage certificate
 * @author asankha
 */
public class MCImageServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MCImageServlet.class);
    private static final String INVALID_ID  = "Invalid ID. Check log for more details";
    private static final String NOT_EXIST   = "A scanned copy does not exist for this record";
    private static final String CANNOT_FIND = "Cannot locate scanned copy. Please report to the admin";
    private static final String ACCESS_DENIED = "Access Denied";

    private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.
    private static final long DEFAULT_EXPIRE_TIME = 2592000000L; // ..ms = 1 month.

    private MarriageRegistrationService mrService;
    private String contentRoot;
    private String contentType;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context =
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        mrService = (MarriageRegistrationService) context.getBean("marriageRegistrationService");
        contentRoot = mrService.getContentRoot();
        contentType = mrService.getContentType();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
        ServletException, IOException {

        User user = null;

        HttpSession session = req.getSession();
        if (session == null) {
            handleException("Invalid session. Please login", null, resp);
        } else {
            user = (User) session.getAttribute(WebConstants.SESSION_USER_BEAN);
            if (user == null) {
                handleException("Invalid user. Please login", null, resp);
            } else {
                if (!user.isAuthorized(Permission.VIEW_SCANNED_MARRIAGE_CERT)) {
                    logger.error("User : {} is not allowed to view scanned certificates", user.getUserId());
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
                    return;
                }
            }
        }

        final String id = req.getParameter("idUKey");
        if (id != null) {

            long idUKey = 0;
            try {
                idUKey = Long.parseLong(id);
            } catch (Exception e) {
                logger.error("Invalid Marriage idUKey : {} used for scanned image fetch", id);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INVALID_ID);
                return;
            }

            final String path = mrService.getImagePathByIdUKey(idUKey, user);
            if (path != null) {

                File file = new File(contentRoot, path);
                if (file.exists()) {
                    serveFile(req, resp, file);
                } else {
                    logger.error("Cannot locate file : " + file.getAbsolutePath());
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, CANNOT_FIND);
                    return;
                }
            } else {
                logger.error("Invalid Marriage idUKey : {} or scanned image not found", idUKey);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_EXIST);
                return;
            }

        } else {
            logger.error("Marriage idUKey not specified for scanned image fetch");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INVALID_ID);
            return;
        }
    }

    private void serveFile(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {

        // Prepare some variables. The ETag is an unique identifier of the file.
        final String fileName = file.getName();
        final long length = file.length();
        final long lastModified = file.lastModified();
        String eTag = fileName + "_" + length + "_" + lastModified;

        // If-None-Match header should contain "*" or ETag. If so, then return 304.
        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && matches(ifNoneMatch, eTag)) {
            response.setHeader("ETag", eTag); // Required in 304.
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
            response.setHeader("ETag", eTag); // Required in 304.
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // Get content type by file name. If unknown, then set the default value.
        String contentType = getServletContext().getMimeType(fileName);
        if (contentType == null) {
            contentType = this.contentType;
        }

        // Initialize response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setHeader("ETag", eTag);
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(length)); // do not try to gzip images
        CommonUtil.copyStreams(new FileInputStream(file), response.getOutputStream());
    }

    /**
     * Returns true if the given match header matches the given value.
     *
     * @param matchHeader The match header.
     * @param toMatch     The value to be matched.
     * @return True if the given match header matches the given value.
     */
    private static boolean matches(String matchHeader, String toMatch) {
        String[] matchValues = matchHeader.split("\\s*,\\s*");
        Arrays.sort(matchValues);
        return Arrays.binarySearch(matchValues, toMatch) > -1
            || Arrays.binarySearch(matchValues, "*") > -1;
    }

    private void handleException(String msg, Exception e, HttpServletResponse resp) {
        if (e != null) {
            logger.error(msg, e);
        } else {
            logger.error(msg);
        }

        resp.setContentType("text/plain");
        resp.setStatus(500);
        try {
            PrintWriter out = resp.getWriter();
            out.print(msg);
            out.flush();
        } catch (IOException ignore) {}
    }
}
