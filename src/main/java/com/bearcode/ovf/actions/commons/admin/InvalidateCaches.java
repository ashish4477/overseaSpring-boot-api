package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.utils.CacheKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by leonid on 02.07.16.
 */
@Controller
@RequestMapping("/admin/InvalidateCaches.htm")
public class InvalidateCaches extends BaseController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCacheManager cacheManager;

    public InvalidateCaches() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/InvalidateCaches.jsp" );
        setPageTitle( "Faces Config List" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("cacheNames")
    public List<String> getCacheNames() {
        return Arrays.asList( 
                CacheKeyConstants.GET_ALL_STATES,
                CacheKeyConstants.GET_ALL_STATE_VOTER_INFORMATION,
                CacheKeyConstants.FIND_ELECTIONS_OF_STATE,
                CacheKeyConstants.FIND_STATE_VOTER_INFORMATION,
                CacheKeyConstants.GET_ALL_ELECTIONS,
                CacheKeyConstants.GET_ALL_EOD_STATES,
                CacheKeyConstants.GET_REGIONS_OF_STATE,
                CacheKeyConstants.GET_REGIONS_OF_STATE_SELECT2,
                CacheKeyConstants.GET_EOD_REGION
        );
    }

    @RequestMapping( method = { RequestMethod.GET, RequestMethod.HEAD } )
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST )
    public String invalidateCaches( HttpServletRequest request, ModelMap model,
                                    @RequestParam(value = "cacheName", required = false) String[] cacheName ) {
        if ( cacheName != null ) {
            for ( String name : cacheName ) {
                Cache cache = cacheManager.getCache(name);
                cache.clear();
            }
            //redisTemplate.opsForHash().getOperations().delete( Arrays.asList( cacheName ));
        }
        return buildModelAndView( request, model );
    }
}
