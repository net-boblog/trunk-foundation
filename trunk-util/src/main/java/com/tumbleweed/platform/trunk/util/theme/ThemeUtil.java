package com.tumbleweed.platform.trunk.util.theme;

import com.tumbleweed.platform.trunk.base.core.util.TrunkConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by mylover on 1/29/16.
 */
public final class ThemeUtil {

    public static String getTheme(HttpServletRequest request) {
        if (request.getParameter(TrunkConstants.TRUNK_UI_THEME_PARAMETER_NAME) != null) {
            return request.getParameter(TrunkConstants.TRUNK_UI_THEME_PARAMETER_NAME);
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(TrunkConstants.TRUNK_UI_THEME_PARAMETER_NAME) != null) {
            return (String) session.getAttribute(TrunkConstants.TRUNK_UI_THEME_PARAMETER_NAME);
        }

        return TrunkConstants.TRUNK_UI_THEME_DEFAULT;
    }
}
