package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String contextPath = ((HttpServletRequest) request).getContextPath();
        String servletPath = ((HttpServletRequest) request).getServletPath();

        if (servletPath.matches("/css.*")) {
            // CSSフォルダ内は認証処理から除外する
            chain.doFilter(request, response);

        } else {
            HttpSession session = ((HttpServletRequest) request).getSession();

            //クエリパラメータからactionとcommandを取得
            String action = request.getParameter(ForwardConst.ACT.getValue());
            String command = request.getParameter(ForwardConst.CMD.getValue());

            //セッションからログインしている従業員の情報を取得
            EmployeeView ev = (EmployeeView) session.getAttribute(AttributeConst.LOGIN_EMP.getValue());

            if (ev == null) {
                //未ログイン

                if (!(ForwardConst.ACT_AUTH.getValue().equals(action)
                        && (ForwardConst.CMD_SHOW_LOGIN.getValue().equals(command)
                                || ForwardConst.CMD_LOGIN.getValue().equals(command)))) {
                    //ログインページの表示またはログイン実行以外はログインページにリダイレクト
                    ((HttpServletResponse) response).sendRedirect(
                            contextPath
                                    + "?action=" + ForwardConst.ACT_AUTH.getValue()
                                    + "&command=" + ForwardConst.CMD_SHOW_LOGIN.getValue());

                    return;

                }
            } else {
                //ログイン済
                if (ForwardConst.ACT_AUTH.getValue().equals(action)) {
                    //認証系Actionを行おうとしている場合

                    if (ForwardConst.CMD_SHOW_LOGIN.getValue().equals(command)) {
                        //ログインページの表示はトップ画面にリダイレクト
                        ((HttpServletResponse) response).sendRedirect(
                                contextPath
                                        + "?action=" + ForwardConst.ACT_SEARCHER.getValue()
                                        + "&command=" + ForwardConst.CMD_INDEX.getValue());

                        return;
                    } else if (ForwardConst.CMD_LOGOUT.getValue().equals(command)) {
                        //ログアウトの実施は許可
                    } else {
                        //上記以外の認証系Actionはエラー画面
                        String forward = String.format("/WEB-INF/views/%s.jsp", "error/unknown");
                        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
                        dispatcher.forward(request, response);

                        return;

                    }

                }
                if (ev != null) {
                    //ログイン済
                    //管理者権限が一般の場合、登録・編集画面に入ろうとしたら、検索画面に移動。
                    if (ev.getAdminFlag() == 0) {
                        if (action.equals(ForwardConst.ACT_PACK.getValue())
                                || action.equals(ForwardConst.ACT_PATDEV.getValue())
                                || action.equals(ForwardConst.ACT_PATEXAM.getValue())
                                || action.equals(ForwardConst.ACT_REGI_TOP.getValue())
                                || action.equals(ForwardConst.ACT_EMP.getValue())) {
                            //検索画面にリダイレクト
                            ((HttpServletResponse) response).sendRedirect(
                                    contextPath
                                            + "?action=" + ForwardConst.ACT_SEARCHER.getValue()
                                            + "&command=" + ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue());
                            return;
                        }
                    } else if (ev.getAdminFlag() == 1) {
                        if (action.equals(ForwardConst.ACT_EMP.getValue())) {
                            //検索画面にリダイレクト
                            ((HttpServletResponse) response).sendRedirect(
                                    contextPath
                                            + "?action=" + ForwardConst.ACT_SEARCHER.getValue()
                                            + "&command=" + ForwardConst.CMD_SEARCH_BY_DEPARTMENT.getValue());
                            return;
                        }
                    }
                }
            }

            //次のフィルタまたはサーブレットを呼び出し
            chain.doFilter(request, response);

        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
