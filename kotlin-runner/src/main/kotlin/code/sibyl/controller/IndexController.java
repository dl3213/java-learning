package code.sibyl.controller;

import code.sibyl.aop.ActionLog;
import code.sibyl.aop.ActionType;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.model.MenuDTO;
import code.sibyl.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public Mono<String> index(final Model model, ServerWebExchange exchange, @RequestParam(defaultValue = "index") String index
//            ,
//                              @AuthenticationPrincipal UserDetails userDetails,
//                              @CurrentSecurityContext(expression = "authentication") Authentication authentication
    ) {
        index = StringUtils.isBlank(index) ? "index" : index;
        System.err.println("首页访问 --->" + index);
        System.err.println(exchange.getRequest().getURI().toString());
        System.err.println("client from = " + exchange.getRequest().getRemoteAddress());
        System.err.println("to server = " + exchange.getRequest().getLocalAddress());
        System.err.println(exchange.getRequest().getSslInfo());
        exchange.getRequest().getHeaders().entrySet().forEach(System.err::println);

//        List<MenuDTO> menuTree = menuTree();
//        model.addAttribute("menuTree", menuTree);

        final String finalIndex = "/index/" + index;
        return Mono.zip(Mono.just(1), SysUserService.getBean().me())
                .map(tuple -> {
                    model.addAttribute("user", tuple.getT2());
                    model.addAttribute("sys_icon", "/static/sibyl.svg");
                    model.addAttribute("alt", "SIBYL");
                    model.addAttribute("systemName", r.systemName());
                    model.addAttribute("title", r.systemName());
                    return finalIndex;
                });
    }

    @ResponseBody
    @RequestMapping(value = "/menu/tree/base", method = {RequestMethod.GET, RequestMethod.POST})
    public Response menuTreeBase() {
        List<MenuDTO> list = Arrays.asList(
                new MenuDTO()
                        .setCode("Home")
                        .setName("Home")
                        .setLinkUrl("/home")
                        .setIsActive(true)
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/home -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M5 12l-2 0l9 -9l9 9l-2 0"/><path d="M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7"/><path
                                                              d="M9 21v-6a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v6"/></svg>
                                                    </span>
                                """.trim())
                        .setHtml("""
                                <li class="nav-item active">
                                                            <a class="nav-link" target-link="/home" href="#">
                                                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/home -->
                                                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24"
                                                                                           height="24" viewBox="0 0 24 24"
                                                                                           stroke-width="2" stroke="currentColor" fill="none"
                                                                                           stroke-linecap="round"
                                                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z"
                                                                                                                         fill="none"/><path
                                                                                              d="M5 12l-2 0l9 -9l9 9l-2 0"/><path
                                                                                              d="M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7"/><path
                                                                                              d="M9 21v-6a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v6"/></svg>
                                                                </span>
                                                                <span class="nav-link-title">
                                                      Home
                                                    </span>
                                                            </a>
                                                        </li>
                                """.trim())
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Interface")
                        .setName("Interface")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/package -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5"/><path d="M12 12l8 -4.5"/><path
                                                              d="M12 12l0 9"/><path d="M12 12l-8 -4.5"/><path d="M16 5.25l-8 4.5"/></svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item dropdown">
                                                            <a class="nav-link dropdown-toggle" href="#navbar-base" data-bs-toggle="dropdown"
                                                               data-bs-auto-close="outside" role="button" aria-expanded="true">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/package -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5"/><path d="M12 12l8 -4.5"/><path
                                                              d="M12 12l0 9"/><path d="M12 12l-8 -4.5"/><path d="M16 5.25l-8 4.5"/></svg>
                                                    </span>
                                                                <span class="nav-link-title"> Interface </span>
                                                            </a>
                                                            <div class="dropdown-menu">
                                                                <div class="dropdown-menu-columns">
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="alerts.html">
                                                                            Alerts
                                                                        </a>
                                                                        <a class="dropdown-item" href="accordion.html">
                                                                            Accordion
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" href="#sidebar-authentication"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Authentication
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a href="sign-in.html" class="dropdown-item">
                                                                                    Sign in
                                                                                </a>
                                                                                <a href="sign-in-link.html" class="dropdown-item">
                                                                                    Sign in link
                                                                                </a>
                                                                                <a href="sign-in-illustration.html" class="dropdown-item">
                                                                                    Sign in with illustration
                                                                                </a>
                                                                                <a href="sign-in-cover.html" class="dropdown-item">
                                                                                    Sign in with cover
                                                                                </a>
                                                                                <a href="sign-up.html" class="dropdown-item">
                                                                                    Sign up
                                                                                </a>
                                                                                <a href="forgot-password.html" class="dropdown-item">
                                                                                    Forgot password
                                                                                </a>
                                                                                <a href="terms-of-service.html" class="dropdown-item">
                                                                                    Terms of service
                                                                                </a>
                                                                                <a href="auth-lock.html" class="dropdown-item">
                                                                                    Lock screen
                                                                                </a>
                                                                                <a href="2-step-verification.html" class="dropdown-item">
                                                                                    2 step verification
                                                                                </a>
                                                                                <a href="2-step-verification-code.html" class="dropdown-item">
                                                                                    2 step verification code
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" href="blank.html">
                                                                            Blank page
                                                                        </a>
                                                                        <a class="dropdown-item" href="badges.html">
                                                                            Badges
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="buttons.html">
                                                                            Buttons
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" href="#sidebar-cards"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Cards
                                                                                <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a href="cards.html" class="dropdown-item">
                                                                                    Sample cards
                                                                                </a>
                                                                                <a href="card-actions.html" class="dropdown-item">
                                                                                    Card actions
                                                                                    <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                                </a>
                                                                                <a href="cards-masonry.html" class="dropdown-item">
                                                                                    Cards Masonry
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" href="carousel.html">
                                                                            Carousel
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="charts.html">
                                                                            Charts
                                                                        </a>
                                                                        <a class="dropdown-item" href="colors.html">
                                                                            Colors
                                                                        </a>
                                                                        <a class="dropdown-item" href="colorpicker.html">
                                                                            Color picker
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="datagrid.html">
                                                                            Data grid
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="datatables.html">
                                                                            Datatables
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="dropdowns.html">
                                                                            Dropdowns
                                                                        </a>
                                                                        <a class="dropdown-item" href="dropzone.html">
                                                                            Dropzone
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" href="#sidebar-error"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Error pages
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a href="error-404.html" class="dropdown-item">
                                                                                    404 page
                                                                                </a>
                                                                                <a href="error-500.html" class="dropdown-item">
                                                                                    500 page
                                                                                </a>
                                                                                <a href="error-maintenance.html" class="dropdown-item">
                                                                                    Maintenance page
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" href="flags.html">
                                                                            Flags
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="inline-player.html">
                                                                            Inline player
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="lightbox.html">
                                                                            Lightbox
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="lists.html">
                                                                            Lists
                                                                        </a>
                                                                        <a class="dropdown-item" href="modals.html">
                                                                            Modal
                                                                        </a>
                                                                        <a class="dropdown-item" href="maps.html">
                                                                            Map
                                                                        </a>
                                                                        <a class="dropdown-item" href="map-fullsize.html">
                                                                            Map fullsize
                                                                        </a>
                                                                        <a class="dropdown-item" href="maps-vector.html">
                                                                            Map vector
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="markdown.html">
                                                                            Markdown
                                                                        </a>
                                                                        <a class="dropdown-item" href="navigation.html">
                                                                            Navigation
                                                                        </a>
                                                                        <a class="dropdown-item" href="offcanvas.html">
                                                                            Offcanvas
                                                                        </a>
                                                                        <a class="dropdown-item" href="pagination.html">
                                                                            <!-- Download SVG icon from http://tabler-icons.io/i/pie-chart -->
                                                                            Pagination
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/placeholder.html" href="#">
                                                                            Placeholder
                                                                        </a>
                                                                        <a class="dropdown-item" href="steps.html">
                                                                            Steps
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="stars-rating.html">
                                                                            Stars rating
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="tabs.html">
                                                                            Tabs
                                                                        </a>
                                                                        <a class="dropdown-item" href="tags.html">
                                                                            Tags
                                                                        </a>
                                                                        <a class="dropdown-item" href="tables.html">
                                                                            Tables
                                                                        </a>
                                                                        <a class="dropdown-item" href="typography.html">
                                                                            Typography
                                                                        </a>
                                                                        <a class="dropdown-item" href="tinymce.html">
                                                                            TinyMCE
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                """)
                        .setChildrenLine(2)
                        .setChildren(Arrays.asList(
                        )),
                new MenuDTO()
                        .setCode("Form elements")
                        .setName("Form elements")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/checkbox -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M9 11l3 3l8 -8"/><path
                                                              d="M20 12v6a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2h9"/></svg>
                                 </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/form-elements.html" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/checkbox -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M9 11l3 3l8 -8"/><path
                                                              d="M20 12v6a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2h9"/></svg>
                                                    </span>
                                                                <span class="nav-link-title">
                                                      Form elements
                                                    </span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/form-elements.html")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Extra")
                        .setName("Extra")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/star -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 17.75l-6.172 3.245l1.179 -6.873l-5 -4.867l6.9 -1l3.086 -6.253l3.086 6.253l6.9 1l-5 4.867l1.179 6.873z"/></svg>
                                </span>
                                """)
                        .setHtml("""
                                <li class="nav-item dropdown">
                                                            <a class="nav-link dropdown-toggle" href="#navbar-extra" data-bs-toggle="dropdown"
                                                               data-bs-auto-close="outside" role="button" aria-expanded="false">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/star -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 17.75l-6.172 3.245l1.179 -6.873l-5 -4.867l6.9 -1l3.086 -6.253l3.086 6.253l6.9 1l-5 4.867l1.179 6.873z"/></svg>
                                                    </span>
                                                                <span class="nav-link-title">
                                                      Extra
                                                    </span>
                                                            </a>
                                                            <div class="dropdown-menu">
                                                                <div class="dropdown-menu-columns">
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="empty.html">
                                                                            Empty page
                                                                        </a>
                                                                        <a class="dropdown-item" href="cookie-banner.html">
                                                                            Cookie banner
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="chat.html">
                                                                            Chat
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="activity.html">
                                                                            Activity
                                                                        </a>
                                                                        <a class="dropdown-item" href="gallery.html">
                                                                            Gallery
                                                                        </a>
                                                                        <a class="dropdown-item" href="invoice.html">
                                                                            Invoice
                                                                        </a>
                                                                        <a class="dropdown-item" href="search-results.html">
                                                                            Search results
                                                                        </a>
                                                                        <a class="dropdown-item" href="pricing.html">
                                                                            Pricing cards
                                                                        </a>
                                                                        <a class="dropdown-item" href="pricing-table.html">
                                                                            Pricing table
                                                                        </a>
                                                                        <a class="dropdown-item" href="faq.html">
                                                                            FAQ
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="users.html">
                                                                            Users
                                                                        </a>
                                                                        <a class="dropdown-item" href="license.html">
                                                                            License
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="logs.html">
                                                                            Logs
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="music.html">
                                                                            Music
                                                                        </a>
                                                                        <a class="dropdown-item" href="photogrid.html">
                                                                            Photogrid
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="tasks.html">
                                                                            Tasks
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="uptime.html">
                                                                            Uptime monitor
                                                                        </a>
                                                                        <a class="dropdown-item" href="widgets.html">
                                                                            Widgets
                                                                        </a>
                                                                        <a class="dropdown-item" href="wizard.html">
                                                                            Wizard
                                                                        </a>
                                                                        <a class="dropdown-item" href="settings.html">
                                                                            Settings
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="trial-ended.html">
                                                                            Trial ended
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="job-listing.html">
                                                                            Job listing
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="page-loader.html">
                                                                            Page loader
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="modal.html" href="#">
                                                                            modal
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                """)
                        .setChildrenLine(2)
                        .setChildren(Arrays.asList(
                                new MenuDTO().setCode("Empty page").setName("Empty page").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Cookie banner").setName("Cookie banner").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Cookie banner").setName("Cookie banner").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Chat").setName("Chat").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Activity").setName("Activity").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Gallery").setName("Gallery").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Invoice").setName("Invoice").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Search results").setName("Search results").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Pricing cards").setName("Pricing cards").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Pricing table").setName("Pricing table").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("FAQ").setName("FAQ").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Users").setName("Users").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("License").setName("License").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Logs").setName("Logs").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Music").setName("Music").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Photogrid").setName("Photogrid").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Tasks").setName("Tasks").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Uptime monitor").setName("Uptime monitor").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Widgets").setName("Widgets").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Wizard").setName("Wizard").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Settings").setName("Settings").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Trial ended").setName("Trial ended").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Job listing").setName("Job listing").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("Page loader").setName("Page loader").setLinkUrl("alerts.html"),
                                new MenuDTO().setCode("modal").setName("modal").setLinkUrl("alerts.html")
                        )),
                new MenuDTO()
                        .setCode("Layout")
                        .setName("Layout")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/layout-2 -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M4 4m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v1a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M4 13m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v3a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M14 4m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v3a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M14 15m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v1a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/></svg>
                                </span>
                                """)
                        .setHtml("""
                                <li class="nav-item dropdown">
                                                            <a class="nav-link dropdown-toggle" href="#navbar-layout" data-bs-toggle="dropdown"
                                                               data-bs-auto-close="outside" role="button" aria-expanded="false">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/layout-2 -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M4 4m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v1a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M4 13m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v3a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M14 4m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v3a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/><path
                                                              d="M14 15m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v1a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z"/></svg>
                                                    </span>
                                                                <span class="nav-link-title">
                                                      Layout
                                                    </span>
                                                            </a>
                                                            <div class="dropdown-menu">
                                                                <div class="dropdown-menu-columns">
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="/?index=layout-horizontal">
                                                                            Horizontal
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-boxed">
                                                                            Boxed
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-vertical">
                                                                            Vertical
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-vertical-transparent">
                                                                            Vertical transparent
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-vertical-right">
                                                                            Right vertical
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-condensed">
                                                                            Condensed
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-combo">
                                                                            Combined
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" href="/?index=layout-navbar-dark">
                                                                            Navbar dark
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-navbar-sticky">
                                                                            Navbar sticky
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-navbar-overlap">
                                                                            Navbar overlap
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-rtl">
                                                                            RTL mode
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-fluid">
                                                                            Fluid
                                                                        </a>
                                                                        <a class="dropdown-item" href="/?index=layout-fluid-vertical">
                                                                            Fluid vertical
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                """)
                        .setLinkUrl("")
                        .setChildrenLine(2)
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("4637 icons")
                        .setName("4637 icons")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/ghost -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M5 11a7 7 0 0 1 14 0v7a1.78 1.78 0 0 1 -3.1 1.4a1.65 1.65 0 0 0 -2.6 0a1.65 1.65 0 0 1 -2.6 0a1.65 1.65 0 0 0 -2.6 0a1.78 1.78 0 0 1 -3.1 -1.4v-7"/><path
                                                              d="M10 10l.01 0"/><path d="M14 10l.01 0"/><path d="M10 14a3.5 3.5 0 0 0 4 0"/></svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/icons.html" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/ghost -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24"
                                                           stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round"
                                                           stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M5 11a7 7 0 0 1 14 0v7a1.78 1.78 0 0 1 -3.1 1.4a1.65 1.65 0 0 0 -2.6 0a1.65 1.65 0 0 1 -2.6 0a1.65 1.65 0 0 0 -2.6 0a1.78 1.78 0 0 1 -3.1 -1.4v-7"/><path
                                                              d="M10 10l.01 0"/><path d="M14 10l.01 0"/><path d="M10 14a3.5 3.5 0 0 0 4 0"/></svg>
                                                    </span>
                                                                <span class="nav-link-title">
                                                      4637 icons
                                                    </span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/icons.html")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Email templates")
                        .setName("Email templates")
                        .setIcon(""" 
                                """)
                        .setHtml(""" 
                                <li class="nav-item">   
                                                  <a class="nav-link" href="emails.html" >
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 9l9 6l9 -6l-9 -6l-9 6" /><path d="M21 9v10a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-10" /><path d="M3 19l6 -6" /><path d="M15 13l6 6" /></svg>
                                                    </span>
                                                    <span class="nav-link-title">
                                                      Email templates
                                                    </span>
                                                  </a>
                                                </li>
                                """)
                        .setLinkUrl("/icons.html")
                        .setChildren(Arrays.asList()),

                new MenuDTO()
                        .setCode("Help")
                        .setName("Help")
                        .setIcon(""" 
                                                                
                                """)
                        .setHtml(""" 
                                <li class="nav-item dropdown">
                                                  <a class="nav-link dropdown-toggle" href="#navbar-help" data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button" aria-expanded="false" >
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/lifebuoy -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 12m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0" /><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" /><path d="M15 15l3.35 3.35" /><path d="M9 15l-3.35 3.35" /><path d="M5.65 5.65l3.35 3.35" /><path d="M18.35 5.65l-3.35 3.35" /></svg>
                                                    </span>
                                                    <span class="nav-link-title">
                                                      Help
                                                    </span>
                                                  </a>
                                                  <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="https://tabler.io/docs" target="_blank" rel="noopener">
                                                      Documentation
                                                    </a>
                                                    <a class="dropdown-item" href="changelog.html">
                                                      Changelog
                                                    </a>
                                                    <a class="dropdown-item" href="https://github.com/tabler/tabler" target="_blank" rel="noopener">
                                                      Source code
                                                    </a>
                                                    <a class="dropdown-item text-pink" href="https://github.com/sponsors/codecalm" target="_blank" rel="noopener">
                                                      <!-- Download SVG icon from http://tabler-icons.io/i/heart -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-inline me-1" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M19.5 12.572l-7.5 7.428l-7.5 -7.428a5 5 0 1 1 7.5 -6.566a5 5 0 1 1 7.5 6.572" /></svg>
                                                      Sponsor project!
                                                    </a>
                                                  </div>
                                                </li>
                                """)
                        .setLinkUrl("/icons.html")
                        .setChildren(Arrays.asList())

        );
        return Response.success(list);
    }

    @ResponseBody
    @RequestMapping(value = "/menu/tree/sys", method = {RequestMethod.GET, RequestMethod.POST})
    public Response menuTreeSys() {
        List<MenuDTO> list = Arrays.asList(

                new MenuDTO()
                        .setCode("Database")
                        .setName("Database")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-database" width="24"
                                                           height="24" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" fill="none"
                                                           stroke-linecap="round" stroke-linejoin="round">
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 6m-8 0a8 3 0 1 0 16 0a8 3 0 1 0 -16 0"/>
                                                          <path d="M4 6v6a8 3 0 0 0 16 0v-6"/><path
                                                              d="M4 12v6a8 3 0 0 0 16 0v-6"/>
                                                      </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/database/list-view" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-database" width="24"
                                                           height="24" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" fill="none"
                                                           stroke-linecap="round" stroke-linejoin="round">
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/><path
                                                              d="M12 6m-8 0a8 3 0 1 0 16 0a8 3 0 1 0 -16 0"/>
                                                          <path d="M4 6v6a8 3 0 0 0 16 0v-6"/><path
                                                              d="M4 12v6a8 3 0 0 0 16 0v-6"/>
                                                      </svg>
                                                    </span>
                                                                <span class="nav-link-title">Database</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/database/list-view")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("File")
                        .setName("File")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-file">
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                      </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/file/list-view" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-file">
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                      </svg>
                                                    </span>
                                                                <span class="nav-link-title">File</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/file/list-view")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Video")
                        .setName("Video")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-movie"><path stroke="none"
                                                                                                                                 d="M0 0h24v24H0z"
                                                                                                                                 fill="none"/><path
                                                              d="M4 4m0 2a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2z"/><path
                                                              d="M8 4l0 16"/><path d="M16 4l0 16"/><path d="M4 8l4 0"/><path d="M4 16l4 0"/><path
                                                              d="M4 12l16 0"/><path d="M16 8l4 0"/><path d="M16 16l4 0"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/file/list-view" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-movie"><path stroke="none"
                                                                                                                                 d="M0 0h24v24H0z"
                                                                                                                                 fill="none"/><path
                                                              d="M4 4m0 2a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2z"/><path
                                                              d="M8 4l0 16"/><path d="M16 4l0 16"/><path d="M4 8l4 0"/><path d="M4 16l4 0"/><path
                                                              d="M4 12l16 0"/><path d="M16 8l4 0"/><path d="M16 16l4 0"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                                                <span class="nav-link-title">Video</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/file/list-view")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Photo")
                        .setName("Photo")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-photo"><path stroke="none"
                                                                                                                                 d="M0 0h24v24H0z"
                                                                                                                                 fill="none"/><path
                                                              d="M15 8h.01"/><path
                                                              d="M3 6a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v12a3 3 0 0 1 -3 3h-12a3 3 0 0 1 -3 -3v-12z"/><path
                                                              d="M3 16l5 -5c.928 -.893 2.072 -.893 3 0l5 5"/><path
                                                              d="M14 14l1 -1c.928 -.893 2.072 -.893 3 0l3 3"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/photo/list-view" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-photo"><path stroke="none"
                                                                                                                                 d="M0 0h24v24H0z"
                                                                                                                                 fill="none"/><path
                                                              d="M15 8h.01"/><path
                                                              d="M3 6a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v12a3 3 0 0 1 -3 3h-12a3 3 0 0 1 -3 -3v-12z"/><path
                                                              d="M3 16l5 -5c.928 -.893 2.072 -.893 3 0l5 5"/><path
                                                              d="M14 14l1 -1c.928 -.893 2.072 -.893 3 0l3 3"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                                                <span class="nav-link-title">Photo</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/photo/list-view")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Gallery")
                        .setName("Gallery")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-photo-plus"><path stroke="none"
                                                                                                                                      d="M0 0h24v24H0z"
                                                                                                                                      fill="none"/><path
                                                              d="M15 8h.01"/><path
                                                              d="M12.5 21h-6.5a3 3 0 0 1 -3 -3v-12a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v6.5"/><path
                                                              d="M3 16l5 -5c.928 -.893 2.072 -.893 3 0l4 4"/><path
                                                              d="M14 14l1 -1c.67 -.644 1.45 -.824 2.182 -.54"/><path d="M16 19h6"/><path d="M19 16v6"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/gallery.html" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-photo-plus"><path stroke="none"
                                                                                                                                      d="M0 0h24v24H0z"
                                                                                                                                      fill="none"/><path
                                                              d="M15 8h.01"/><path
                                                              d="M12.5 21h-6.5a3 3 0 0 1 -3 -3v-12a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v6.5"/><path
                                                              d="M3 16l5 -5c.928 -.893 2.072 -.893 3 0l4 4"/><path
                                                              d="M14 14l1 -1c.67 -.644 1.45 -.824 2.182 -.54"/><path d="M16 19h6"/><path d="M19 16v6"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                                                <span class="nav-link-title">Gallery</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/gallery.html")
                        .setChildren(Arrays.asList()),
                new MenuDTO()
                        .setCode("Recycle")
                        .setName("Recycle")
                        .setIcon("""
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                                           stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                                           class="icon icon-tabler icons-tabler-outline icon-tabler-photo-plus"><path stroke="none"
                                                                                                                                      d="M0 0h24v24H0z"
                                                                                                                                      fill="none"/><path
                                                              d="M15 8h.01"/><path
                                                              d="M12.5 21h-6.5a3 3 0 0 1 -3 -3v-12a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v6.5"/><path
                                                              d="M3 16l5 -5c.928 -.893 2.072 -.893 3 0l4 4"/><path
                                                              d="M14 14l1 -1c.67 -.644 1.45 -.824 2.182 -.54"/><path d="M16 19h6"/><path d="M19 16v6"/></svg>
                                                          <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                          <path d="M14 3v4a1 1 0 0 0 1 1h4"/>
                                                          <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"/>
                                                        </svg>
                                                    </span>
                                """)
                        .setHtml("""
                                <li class="nav-item">
                                                            <a class="nav-link" target-link="/recycle.html" href="#">
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                                      <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-trash-x"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M4 7h16" /><path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" /><path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" /><path d="M10 12l4 4m0 -4l-4 4" /></svg>
                                                    </span>
                                                                <span class="nav-link-title">Recycle</span>
                                                            </a>
                                                        </li>
                                """)
                        .setLinkUrl("/recycle.html")
                        .setChildren(Arrays.asList())

        );
        return Response.success(list);
    }

    @GetMapping({"home",})
    @ActionLog(topic = "home首页", type = ActionType.OTHER)
    public Mono<String> home(final Model model) {
        String welcome = "/index/home";
        model.addAttribute("app-pid", ProcessHandle.current().pid());
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
//        String s = "sign-in";
//        model.addAttribute("systemName", r.systemName());
//        model.addAttribute("title", r.systemName());
//        return Mono.create(monoSink -> monoSink.success(s));
        String s = "sign-in";
        return exchange.getSession().flatMap(webSession -> {
            model.addAttribute("systemName", r.systemName());
            model.addAttribute("title", r.systemName());
            model.addAttribute("msg", webSession.getAttributes().get("redirect-msg"));
            return Mono.just(s);
        });
    }

    @GetMapping({"sign-up.html"})
    public Mono<String> sign_up(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "sign-up";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"profile.html"})
    public Mono<String> profile(final Model model, ServerWebExchange exchange, ServerHttpRequest request) {
        String s = "profile";
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
        String s = "/index/layout-vertical";
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
        String s = "gallery/list-view";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }
    @GetMapping({"/recycle.html"})
    public Mono<String> recycle(final Model model) {
        String s = "file/recycle-list-view";
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
        String s = "/index/layout-horizontal";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-boxed.html"})
    public Mono<String> layout_boxed(final Model model) {
        String s = "/index/layout-boxed";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical-transparent.html"})
    public Mono<String> layout_vertical_transparent(final Model model) {
        String s = "/index/layout-vertical-transparent";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-vertical-right.html"})
    public Mono<String> layout_vertical_right(final Model model) {
        String s = "/index/layout-vertical-right";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-condensed.html"})
    public Mono<String> layout_condensed(final Model model) {
        String s = "/index/layout-condensed";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-combo.html"})
    public Mono<String> layout_combo(final Model model) {
        String s = "/index/layout-combo";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-dark.html"})
    public Mono<String> layout_navbar_dark(final Model model) {
        String s = "/index/layout-navbar-dark";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-sticky.html"})
    public Mono<String> layout_navbar_sticky(final Model model) {
        String s = "/index/layout-navbar-sticky";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-navbar-overlap.html"})
    public Mono<String> layout_navbar_overlap(final Model model) {
        String s = "/index/layout-navbar-overlap";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-rtl.html"})
    public Mono<String> layout_rtl(final Model model) {
        String s = "/index/layout-rtl";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-fluid.html"})
    public Mono<String> layout_fluid(final Model model) {
        String s = "/index/layout-fluid";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping({"/layout-fluid-vertical.html"})
    public Mono<String> layout_fluid_vertical(final Model model) {
        String s = "/index/layout-fluid-vertical";
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
