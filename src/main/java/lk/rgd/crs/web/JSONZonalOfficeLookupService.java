package lk.rgd.crs.web;

import lk.rgd.common.api.dao.ZonalOfficesDAO;
import lk.rgd.common.api.domain.ZonalOffice;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Duminda Dharmakeerthi
 */
public class JSONZonalOfficeLookupService extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JSONZonalOfficeLookupService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private ZonalOfficesDAO zonalOfficesDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        zonalOfficesDAO = (ZonalOfficesDAO) context.getBean("zonalOfficeDAOImpl");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter(WebConstants.ZONAL_OFFICE_ID);
        HashMap<String, Object> optionLists;

        try{
            optionLists = new HashMap<String, Object>();
            int zonalOfficeId = Integer.parseInt(id);
            logger.info("Zonal Office Id: {}", zonalOfficeId);
            ZonalOffice zonalOffice = zonalOfficesDAO.getZonalOffice(zonalOfficeId);

            optionLists.put("siAddress", zonalOffice.getSiZonalOfficeName()+",\n"+ zonalOffice.getSiZonalOfficeMailAddress());
            optionLists.put("taAddress", zonalOffice.getTaZonalOfficeName()+",\n"+ zonalOffice.getTaZonalOfficeMailAddress());
            optionLists.put("enAddress", zonalOffice.getEnZonalOfficeName()+",\n"+ zonalOffice.getEnZonalOfficeMailAddress());
            optionLists.put("telephone", zonalOffice.getZonalOfficeLandPhone());
        }catch (Exception e){
            logger.error("Fatal Error : {}", e);
            return;
        }
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();

        mapper.writeValue(out, optionLists);
        out.flush();
    }
}
