package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductDetailsPageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ServletConfig config;

    @Mock
    private ServletContextEvent event;

    @Mock
            ServletContext context;

    DemoDataServletContextListener listener = new DemoDataServletContextListener();

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(event.getServletContext()).thenReturn(context);
        when(event.getServletContext().getInitParameter(anyString())).thenReturn("true");
        listener.contextInitialized(event);

        servlet.init(config);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);



    }

    @Test
    public void testDoGet() throws ServletException, IOException {

        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}