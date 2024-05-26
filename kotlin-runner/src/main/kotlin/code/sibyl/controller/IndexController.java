package code.sibyl.controller;

import code.sibyl.common.r;
import code.sibyl.model.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @RequestMapping({"/", "index", "main"})
    public Mono<String> index(final Model model, @AuthenticationPrincipal UserDetails userDetails, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        //System.err.println("首页");
        List<String> authorities = userDetails.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList());
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("authentication", authentication);
        model.addAttribute("authorities", authorities);
        List<MenuDTO> menuTree = menuTree();
        model.addAttribute("menuTree", menuTree);
        return Mono.create(monoSink -> monoSink.success("index"));
    }

    private static List<MenuDTO> menuTree() {
        return Arrays.asList(
                new MenuDTO()
                        .setCode("system")
                        .setName("系统管理")
                        .setChildren(Arrays.asList(new MenuDTO().setName("运行状态").setLinkUrl("sys/main-view"), new MenuDTO().setName("控制面板").setLinkUrl("sys/control-view"))),
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
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping("welcome")
    public Mono<String> toWelcome() {
        String welcome = "default/welcome";
        return Mono.create(monoSink -> monoSink.success(welcome));
    }

    @GetMapping({"sign-in.html"})
    public Mono<String> sign_in(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
//        System.err.println("sign-in");
//        System.err.println(exchange);
//        System.err.println(exchange.getRequest().getPath());
//        System.err.println(exchange.getRequest().getQueryParams());
//        System.err.println(exchange.getFormData());
//        System.err.println(exchange.getMultipartData());
//        System.err.println(request);
        String s = "sign-in";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"sign-up.html"})
    public Mono<String> sign_up(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "sign-up";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"sign-in-link.html"})
    public Mono<String> sign_in_link(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "sign-in-link";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"sign-in-illustration.html"})
    public Mono<String> sign_in_illustration(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "sign-in-illustration";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"sign-in-cover.html"})
    public Mono<String> sign_in_cover(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "sign-in-cover";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"forgot-password.html"})
    public Mono<String> forgot_password(final Model model) {
        String s = "forgot-password";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"auth-lock.html"})
    public Mono<String> auth_lock(final Model model) {
        String s = "auth-lock";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"terms-of-service.html"})
    public Mono<String> terms_of_service(final Model model) {
        String s = "terms-of-service";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"2-step-verification.html"})
    public Mono<String> _2_step_verification(final Model model) {
        String s = "2-step-verification";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"2-step-verification-code.html"})
    public Mono<String> _2_step_verification_code(final Model model) {
        String s = "2-step-verification-code";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"settings.html"})
    public Mono<String> settings(final Model model) {
        String s = "settings";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/form-elements.html"})
    public Mono<String> form_elements(final Model model) {
        String s = "form-elements";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical.html"})
    public Mono<String> layout_vertical(final Model model) {
        String s = "layout-vertical";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/uptime.html"})
    public Mono<String> uptime(final Model model) {
        String s = "uptime";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/lists.html"})
    public Mono<String> lists(final Model model) {
        String s = "lists";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/tables.html"})
    public Mono<String> tables(final Model model) {
        String s = "tables";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/emails.html"})
    public Mono<String> emails(final Model model) {
        String s = "emails";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/database.html"})
    public Mono<String> database(final Model model) {
        String s = "database";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/buttons.html"})
    public Mono<String> buttons(final Model model) {
        String s = "buttons";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/cards.html"})
    public Mono<String> cards(final Model model) {
        String s = "cards";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/card-actions.html"})
    public Mono<String> card_aactions(final Model model) {
        String s = "card-actions";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/cards-masonry.html"})
    public Mono<String> cards_masonry(final Model model) {
        String s = "cards-masonry";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/msg"})
    public Mono<String> msg(final Model model) {
        String s = "common/msg";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/chat.html"})
    public Mono<String> chat(final Model model) {
        String s = "chat";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/empty.html"})
    public Mono<String> empty(final Model model) {
        String s = "empty";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/cookie-banner.html"})
    public Mono<String> cookie_banner(final Model model) {
        String s = "cookie-banner";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/activity.html"})
    public Mono<String> activity(final Model model) {
        String s = "activity";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/gallery.html"})
    public Mono<String> gallery(final Model model) {
        String s = "gallery";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/invoice.html"})
    public Mono<String> invoice(final Model model) {
        String s = "invoice";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/search-results.html"})
    public Mono<String> search_results(final Model model) {
        String s = "search-results";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/pricing.html"})
    public Mono<String> pricing(final Model model) {
        String s = "pricing";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/pricing-table.html"})
    public Mono<String> pricing_table(final Model model) {
        String s = "pricing-table";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/faq.html"})
    public Mono<String> faq(final Model model) {
        String s = "faq";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/users.html"})
    public Mono<String> users(final Model model) {
        String s = "users";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/license.html"})
    public Mono<String> license(final Model model) {
        String s = "license";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/logs.html"})
    public Mono<String> logs(final Model model) {
        String s = "logs";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/music.html"})
    public Mono<String> music(final Model model) {
        String s = "music";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/photogrid.html"})
    public Mono<String> photogrid(final Model model) {
        String s = "photogrid";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/tasks.html"})
    public Mono<String> tasks(final Model model) {
        String s = "tasks";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/widgets.html"})
    public Mono<String> widgets(final Model model) {
        String s = "widgets";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/wizard.html"})
    public Mono<String> wizard(final Model model) {
        String s = "wizard";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/trial-ended.html"})
    public Mono<String> trial_ended(final Model model) {
        String s = "trial-ended";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/job-listing.html"})
    public Mono<String> job_listing(final Model model) {
        String s = "job-listing";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/page-loader.html"})
    public Mono<String> page_loader(final Model model) {
        String s = "page-loader";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/icons.html"})
    public Mono<String> icons(final Model model) {
        String s = "icons";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }


    @GetMapping({"/layout-horizontal.html"})
    public Mono<String> layout_horizontal(final Model model) {
        String s = "layout-horizontal";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-boxed.html"})
    public Mono<String> layout_boxed(final Model model) {
        String s = "layout-boxed";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical-transparent.html"})
    public Mono<String> layout_vertical_transparent(final Model model) {
        String s = "layout-vertical-transparent";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical-right.html"})
    public Mono<String> layout_vertical_right(final Model model) {
        String s = "layout-vertical-right";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-condensed.html"})
    public Mono<String> layout_condensed(final Model model) {
        String s = "layout-condensed";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-combo.html"})
    public Mono<String> layout_combo(final Model model) {
        String s = "layout-combo";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-dark.html"})
    public Mono<String> layout_navbar_dark(final Model model) {
        String s = "layout-navbar-dark";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-sticky.html"})
    public Mono<String> layout_navbar_sticky(final Model model) {
        String s = "layout-navbar-sticky";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-overlap.html"})
    public Mono<String> layout_navbar_overlap(final Model model) {
        String s = "layout-navbar-overlap";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-rtl.html"})
    public Mono<String> layout_rtl(final Model model) {
        String s = "layout-rtl";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-fluid.html"})
    public Mono<String> layout_fluid(final Model model) {
        String s = "layout-fluid";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-fluid-vertical.html"})
    public Mono<String> layout_fluid_vertical(final Model model) {
        String s = "layout-fluid-vertical";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/alerts.html"})
    public Mono<String> alerts(final Model model) {
        String s = "alerts";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/accordion.html"})
    public Mono<String> accordion(final Model model) {
        String s = "accordion";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/blank.html"})
    public Mono<String> blank(final Model model) {
        String s = "blank";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/badges.html"})
    public Mono<String> badges(final Model model) {
        String s = "badges";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/carousel.html"})
    public Mono<String> carousel(final Model model) {
        String s = "carousel";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/charts.html"})
    public Mono<String> charts(final Model model) {
        String s = "charts";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/colors.html"})
    public Mono<String> colors(final Model model) {
        String s = "colors";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/colorpicker.html"})
    public Mono<String> colorpicker(final Model model) {
        String s = "colorpicker";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/datagrid.html"})
    public Mono<String> datagrid(final Model model) {
        String s = "datagrid";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/datatables.html"})
    public Mono<String> datatables(final Model model) {
        String s = "datatables";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/dropdowns.html"})
    public Mono<String> dropdowns(final Model model) {
        String s = "dropdowns";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/dropzone.html"})
    public Mono<String> dropzone(final Model model) {
        String s = "dropzone";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/error-404.html"})
    public Mono<String> error_404(final Model model) {
        String s = "error-404";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/error-500.html"})
    public Mono<String> error_500(final Model model) {
        String s = "error-500";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/error-maintenance.html"})
    public Mono<String> error_maintenance(final Model model) {
        String s = "error-maintenance";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/flags.html"})
    public Mono<String> flags(final Model model) {
        String s = "flags";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/inline-player.html"})
    public Mono<String> inline_player(final Model model) {
        String s = "inline-player";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/maps.html"})
    public Mono<String> maps(final Model model) {
        String s = "maps";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/map-fullsize.html"})
    public Mono<String> map_fullsize(final Model model) {
        String s = "map-fullsize";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/maps-vector.html"})
    public Mono<String> maps_vector(final Model model) {
        String s = "maps-vector";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/lightbox.html"})
    public Mono<String> lightbox(final Model model) {
        String s = "lightbox";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/markdown.html"})
    public Mono<String> markdown(final Model model) {
        String s = "markdown";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/navigation.html"})
    public Mono<String> navigation(final Model model) {
        String s = "navigation";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/offcanvas.html"})
    public Mono<String> offcanvas(final Model model) {
        String s = "offcanvas";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/pagination.html"})
    public Mono<String> pagination(final Model model) {
        String s = "pagination";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/placeholder.html"})
    public Mono<String> placeholder(final Model model) {
        String s = "placeholder";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/steps.html"})
    public Mono<String> steps(final Model model) {
        String s = "steps";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/stars-rating.html"})
    public Mono<String> stars_rating(final Model model) {
        String s = "stars-rating";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tabs.html"})
    public Mono<String> tabs(final Model model) {
        String s = "tabs";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tags.html"})
    public Mono<String> tags(final Model model) {
        String s = "tags";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/typography.html"})
    public Mono<String> typography(final Model model) {
        String s = "typography";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/tinymce.html"})
    public Mono<String> tinymce(final Model model) {
        String s = "tinymce";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/modal.html"})
    public Mono<String> modal(final Model model) {
        String s = "modals";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/modals.html"})
    public Mono<String> modals(final Model model) {
        String s = "modals";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
}
