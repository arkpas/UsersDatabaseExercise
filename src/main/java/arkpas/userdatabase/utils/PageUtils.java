package arkpas.userdatabase.utils;

import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpSession;


public class PageUtils {

    public static final int DEFAULT_NUMBER_OF_RESULTS = 5;
    public static final int MAX_NUMBER_OF_RESULTS = 100;
    public static final String DEFAULT_SORT_BY = "birthDate";
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    public static int getNumberOfResultsOnPage (HttpSession httpSession) {
        if (httpSession == null)
            return DEFAULT_NUMBER_OF_RESULTS;

        int numberOfResults = DEFAULT_NUMBER_OF_RESULTS;
        Object object = httpSession.getAttribute("numberOfResults");
        if (object instanceof Integer) {
            numberOfResults = (int) object;
        }

        if (numberOfResults > 0 && numberOfResults <= MAX_NUMBER_OF_RESULTS)
            return numberOfResults;
        else
            return DEFAULT_NUMBER_OF_RESULTS;
    }

    public static String getSortBy (HttpSession httpSession) {
        if (httpSession == null)
            return DEFAULT_SORT_BY;

        String sortBy = (String) httpSession.getAttribute("sortBy");
        if (sortBy == null)
            sortBy = DEFAULT_SORT_BY;
        return sortBy;
    }

    public static Sort.Direction getSortDirection (String sortBy) {
        if (sortBy != null && sortBy.equals("birthDate"))
            return Sort.Direction.DESC;
        else
            return Sort.Direction.ASC;
    }


}
