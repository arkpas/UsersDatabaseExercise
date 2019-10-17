package unit.utils;

import arkpas.userdatabase.utils.PageUtils;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;

public class PageUtilsTest {

    @Test
    public void getNumberOfResultsOnPageShouldReturnDefaultValue () {
        HttpSession httpSession = new MockHttpSession();
        assertEquals(PageUtils.DEFAULT_NUMBER_OF_RESULTS, PageUtils.getNumberOfResultsOnPage(httpSession));
    }

    @Test
    public void getNumberOfResultsOnPageShouldReturnDefaultValueIfArgumentIsNull () {
        HttpSession httpSession = null;
        assertEquals(PageUtils.DEFAULT_NUMBER_OF_RESULTS, PageUtils.getNumberOfResultsOnPage(httpSession));
    }

    @Test
    public void getNumberOfResultsOnPageShouldReturnUserValue () {
        HttpSession httpSession = new MockHttpSession();
        int userNumberOfResults = 10;
        httpSession.setAttribute("numberOfResults", userNumberOfResults);
        assertEquals(userNumberOfResults, PageUtils.getNumberOfResultsOnPage(httpSession));
    }

    @Test
    public void getNumberOfResultsOnPageShouldReturnDefaultValueIfUserValueIsGreaterThanMax () {
        HttpSession httpSession = new MockHttpSession();
        int userNumberOfResults = PageUtils.MAX_NUMBER_OF_RESULTS + 1;
        httpSession.setAttribute("numberOfResults", userNumberOfResults);
        assertEquals(PageUtils.DEFAULT_NUMBER_OF_RESULTS, PageUtils.getNumberOfResultsOnPage(httpSession));
    }

    @Test
    public void getNumberOfResultsOnPageShouldReturnDefaultValueIfUserValueIsLessThanZero () {
        HttpSession httpSession = new MockHttpSession();
        int userNumberOfResults = -1;
        httpSession.setAttribute("numberOfResults", userNumberOfResults);
        assertEquals(PageUtils.DEFAULT_NUMBER_OF_RESULTS, PageUtils.getNumberOfResultsOnPage(httpSession));
    }

    @Test
    public void getSortByShouldReturnDefaultValue () {
        HttpSession httpSession = new MockHttpSession();
        assertEquals(PageUtils.DEFAULT_SORT_BY, PageUtils.getSortBy(httpSession));
    }

    @Test
    public void getSortByShouldReturnDefaultValueWhenArgumentIsNull () {
        HttpSession httpSession = null;
        assertEquals(PageUtils.DEFAULT_SORT_BY, PageUtils.getSortBy(httpSession));
    }

    @Test
    public void getSortByShouldReturnUserValue () {
        HttpSession httpSession = new MockHttpSession();
        String userValue = "name";
        httpSession.setAttribute("sortBy", userValue);
        assertEquals(userValue, PageUtils.getSortBy(httpSession));
    }

    @Test
    public void getSortDirectionShouldReturnDescWhenSortedByBirthDate () {
        String sortBy = "birthDate";
        assertEquals(Sort.Direction.DESC, PageUtils.getSortDirection(sortBy));
    }

    @Test
    public void getSortDirectionShouldReturnAscWhenSortedByAnyOtherThanBirthDate () {
        String sortBy = "any";
        assertEquals(Sort.Direction.ASC, PageUtils.getSortDirection(sortBy));
    }
}
