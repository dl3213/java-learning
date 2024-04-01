package code.sibyl.controller;

import code.sibyl.dto.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final static String systemName = "未命名";

    @RequestMapping({"/", "index", "main"})
    public Mono<String> index(final Model model, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
//        System.err.println("首页");
//        System.err.println(userDetails);
//        System.err.println(databaseClient);
//        System.err.println(r2dbcEntityTemplate);
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        model.addAttribute("username", userDetails.getUsername());
        List<MenuDTO> menuTree = menuTree();
        model.addAttribute("menuTree", menuTree);
        return Mono.create(monoSink -> monoSink.success("index"));
    }

    private static List<MenuDTO> menuTree() {
        return Arrays.asList(
                new MenuDTO()
                        .setCode("system")
                        .setName("系统管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("运行状态").setLinkUrl("sys/main-view"),new MenuDTO().setName("控制面板").setLinkUrl("sys/control-view"))),
                new MenuDTO()
                        .setCode("sys-user")
                        .setName("用户管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("用户列表").setLinkUrl("user/list-view"))),
                new MenuDTO()
                        .setCode("sys-config")
                        .setName("配置管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("配置列表").setLinkUrl("config/list-view"))),
                new MenuDTO()
                        .setCode("database")
                        .setName("数据库管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("数据库列表").setLinkUrl("database/list-view")))
        );
    }

    @GetMapping({"home",})
    public Mono<String> home() {
        String welcome = "home";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"sys/main",})
    public Mono<String> sys_main() {
        String welcome = "default/welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"pages/main",})
    public Mono<String> page_main() {
        String s = "default/pages/main";
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/login-view"})
    public Mono<String> login_view(final Model model) {
        String s = "login-view";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping("welcome")
    public Mono<String> toWelcome() {
        String welcome = "default/welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"sign-in.html"})
    public Mono<String> sign_in(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        System.err.println("sign-in");
        System.err.println(exchange);
        System.err.println(exchange.getRequest().getPath());
        System.err.println(exchange.getRequest().getQueryParams());
        System.err.println(exchange.getFormData());
        System.err.println(exchange.getMultipartData());
        System.err.println(request);
        String s = "sign-in";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"settings.html"})
    public Mono<String> settings(final Model model) {
        String s = "settings";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/form-elements.html"})
    public Mono<String> form_elements(final Model model) {
        String s = "form-elements";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical.html"})
    public Mono<String> layout_vertical(final Model model) {
        String s = "layout-vertical";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/uptime.html"})
    public Mono<String> uptime(final Model model) {
        String s = "uptime";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/lists.html"})
    public Mono<String> lists(final Model model) {
        String s = "lists";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tables.html"})
    public Mono<String> tables(final Model model) {
        String s = "tables";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/emails.html"})
    public Mono<String> emails(final Model model) {
        String s = "emails";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/database.html"})
    public Mono<String> database(final Model model) {
        String s = "database";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/buttons.html"})
    public Mono<String> buttons(final Model model) {
        String s = "buttons";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/msg"})
    public Mono<String> msg(final Model model) {
        String s = "common/msg";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/chat.html"})
    public Mono<String> chat(final Model model) {
        String s = "chat";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/empty.html"})
    public Mono<String> empty(final Model model) {
        String s = "empty";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/cookie-banner.html"})
    public Mono<String> cookie_banner(final Model model) {
        String s = "cookie-banner";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/activity.html"})
    public Mono<String> activity(final Model model) {
        String s = "activity";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/gallery.html"})
    public Mono<String> gallery(final Model model) {
        String s = "gallery";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/invoice.html"})
    public Mono<String> invoice(final Model model) {
        String s = "invoice";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/search-results.html"})
    public Mono<String> search_results(final Model model) {
        String s = "search-results";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/pricing.html"})
    public Mono<String> pricing(final Model model) {
        String s = "pricing";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/pricing-table.html"})
    public Mono<String> pricing_table(final Model model) {
        String s = "pricing-table";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/faq.html"})
    public Mono<String> faq(final Model model) {
        String s = "faq";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/users.html"})
    public Mono<String> users(final Model model) {
        String s = "users";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/license.html"})
    public Mono<String> license(final Model model) {
        String s = "license";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/logs.html"})
    public Mono<String> logs(final Model model) {
        String s = "logs";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/music.html"})
    public Mono<String> music(final Model model) {
        String s = "music";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/photogrid.html"})
    public Mono<String> photogrid(final Model model) {
        String s = "photogrid";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tasks.html"})
    public Mono<String> tasks(final Model model) {
        String s = "tasks";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/widgets.html"})
    public Mono<String> widgets(final Model model) {
        String s = "widgets";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/wizard.html"})
    public Mono<String> wizard(final Model model) {
        String s = "wizard";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/trial-ended.html"})
    public Mono<String> trial_ended(final Model model) {
        String s = "trial-ended";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/job-listing.html"})
    public Mono<String> job_listing(final Model model) {
        String s = "job-listing";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/page-loader.html"})
    public Mono<String> page_loader(final Model model) {
        String s = "page-loader";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/icons.html"})
    public Mono<String> icons(final Model model) {
        String s = "icons";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }



    @GetMapping({"/layout-horizontal.html"})
    public Mono<String> layout_horizontal(final Model model) {
        String s = "layout-horizontal";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-boxed.html"})
    public Mono<String> layout_boxed(final Model model) {
        String s = "layout-boxed";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-vertical-transparent.html"})
    public Mono<String> layout_vertical_transparent(final Model model) {
        String s = "layout-vertical-transparent";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-vertical-right.html"})
    public Mono<String> layout_vertical_right(final Model model) {
        String s = "layout-vertical-right";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-condensed.html"})
    public Mono<String> layout_condensed(final Model model) {
        String s = "layout-condensed";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-combo.html"})
    public Mono<String> layout_combo(final Model model) {
        String s = "layout-combo";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-navbar-dark.html"})
    public Mono<String> layout_navbar_dark(final Model model) {
        String s = "layout-navbar-dark";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-navbar-sticky.html"})
    public Mono<String> layout_navbar_sticky(final Model model) {
        String s = "layout-navbar-sticky";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-navbar-overlap.html"})
    public Mono<String> layout_navbar_overlap(final Model model) {
        String s = "layout-navbar-overlap";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-rtl.html"})
    public Mono<String> layout_rtl(final Model model) {
        String s = "layout-rtl";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-fluid.html"})
    public Mono<String> layout_fluid(final Model model) {
        String s = "layout-fluid";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/layout-fluid-vertical.html"})
    public Mono<String> layout_fluid_vertical(final Model model) {
        String s = "layout-fluid-vertical";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/alerts.html"})
    public Mono<String> alerts(final Model model) {
        String s = "alerts";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/accordion.html"})
    public Mono<String> accordion(final Model model) {
        String s = "accordion";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/blank.html"})
    public Mono<String> blank(final Model model) {
        String s = "blank";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/badges.html"})
    public Mono<String> badges(final Model model) {
        String s = "badges";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/carousel.html"})
    public Mono<String> carousel(final Model model) {
        String s = "carousel";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/charts.html"})
    public Mono<String> charts(final Model model) {
        String s = "charts";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/colors.html"})
    public Mono<String> colors(final Model model) {
        String s = "colors";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/colorpicker.html"})
    public Mono<String> colorpicker(final Model model) {
        String s = "colorpicker";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/datagrid.html"})
    public Mono<String> datagrid(final Model model) {
        String s = "datagrid";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/datatables.html"})
    public Mono<String> datatables(final Model model) {
        String s = "datatables";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/dropdowns.html"})
    public Mono<String> dropdowns(final Model model) {
        String s = "dropdowns";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/dropzone.html"})
    public Mono<String> dropzone(final Model model) {
        String s = "dropzone";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/flags.html"})
    public Mono<String> flags(final Model model) {
        String s = "flags";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/inline-player.html"})
    public Mono<String> inline_player(final Model model) {
        String s = "inline-player";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/maps.html"})
    public Mono<String> maps(final Model model) {
        String s = "maps";
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success(s));
    }
}
