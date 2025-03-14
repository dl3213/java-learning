package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.model.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/rest/v1")
@RequiredArgsConstructor
public class IndexRestController {

    @RequestMapping(value = "/menu/tree/base", method = {RequestMethod.GET, RequestMethod.POST})
    public Response menuTreeBase() {
        List<MenuDTO> list = Arrays.asList(
                new MenuDTO()
                        .setCode("Home")
                        .setName("Home")
                        .setIsActive(true)
                        .setHtml("""
                                <li class="nav-item active">
                                                            <a class="nav-link" target-link="/" target-link="/" >
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
                                                            <a class="nav-link dropdown-toggle"  data-bs-toggle="dropdown"
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
                                                                        <a class="dropdown-item" target-link="alerts.html">
                                                                            Alerts
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="accordion.html">
                                                                            Accordion
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" target-link="#sidebar-authentication"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Authentication
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a target-link="sign-in.html" class="dropdown-item">
                                                                                    Sign in
                                                                                </a>
                                                                                <a target-link="sign-in-link.html" class="dropdown-item">
                                                                                    Sign in link
                                                                                </a>
                                                                                <a target-link="sign-in-illustration.html" class="dropdown-item">
                                                                                    Sign in with illustration
                                                                                </a>
                                                                                <a target-link="sign-in-cover.html" class="dropdown-item">
                                                                                    Sign in with cover
                                                                                </a>
                                                                                <a target-link="sign-up.html" class="dropdown-item">
                                                                                    Sign up
                                                                                </a>
                                                                                <a target-link="forgot-password.html" class="dropdown-item">
                                                                                    Forgot password
                                                                                </a>
                                                                                <a target-link="terms-of-service.html" class="dropdown-item">
                                                                                    Terms of service
                                                                                </a>
                                                                                <a target-link="auth-lock.html" class="dropdown-item">
                                                                                    Lock screen
                                                                                </a>
                                                                                <a target-link="2-step-verification.html" class="dropdown-item">
                                                                                    2 step verification
                                                                                </a>
                                                                                <a target-link="2-step-verification-code.html" class="dropdown-item">
                                                                                    2 step verification code
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" target-link="blank.html">
                                                                            Blank page
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="badges.html">
                                                                            Badges
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="buttons.html">
                                                                            Buttons
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" target-link="#sidebar-cards"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Cards
                                                                                <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a target-link="cards.html" class="dropdown-item">
                                                                                    Sample cards
                                                                                </a>
                                                                                <a target-link="card-actions.html" class="dropdown-item">
                                                                                    Card actions
                                                                                    <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                                </a>
                                                                                <a target-link="cards-masonry.html" class="dropdown-item">
                                                                                    Cards Masonry
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" target-link="carousel.html">
                                                                            Carousel
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="charts.html">
                                                                            Charts
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="colors.html">
                                                                            Colors
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="colorpicker.html">
                                                                            Color picker
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="datagrid.html">
                                                                            Data grid
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="datatables.html">
                                                                            Datatables
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="dropdowns.html">
                                                                            Dropdowns
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="dropzone.html">
                                                                            Dropzone
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <div class="dropend">
                                                                            <a class="dropdown-item dropdown-toggle" target-link="#sidebar-error"
                                                                               data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button"
                                                                               aria-expanded="false">
                                                                                Error pages
                                                                            </a>
                                                                            <div class="dropdown-menu">
                                                                                <a target-link="error-404.html" class="dropdown-item">
                                                                                    404 page
                                                                                </a>
                                                                                <a target-link="error-500.html" class="dropdown-item">
                                                                                    500 page
                                                                                </a>
                                                                                <a target-link="error-maintenance.html" class="dropdown-item">
                                                                                    Maintenance page
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                        <a class="dropdown-item" target-link="flags.html">
                                                                            Flags
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="inline-player.html">
                                                                            Inline player
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" target-link="lightbox.html">
                                                                            Lightbox
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="lists.html">
                                                                            Lists
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="modals.html">
                                                                            Modal
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="maps.html">
                                                                            Map
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="map-fullsize.html">
                                                                            Map fullsize
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="maps-vector.html">
                                                                            Map vector
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="markdown.html">
                                                                            Markdown
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="navigation.html">
                                                                            Navigation
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="offcanvas.html">
                                                                            Offcanvas
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="pagination.html">
                                                                            <!-- Download SVG icon from http://tabler-icons.io/i/pie-chart -->
                                                                            Pagination
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/placeholder.html" >
                                                                            Placeholder
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="steps.html">
                                                                            Steps
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="stars-rating.html">
                                                                            Stars rating
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="tabs.html">
                                                                            Tabs
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="tags.html">
                                                                            Tags
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="tables.html">
                                                                            Tables
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="typography.html">
                                                                            Typography
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="tinymce.html">
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
                                                            <a class="nav-link" target-link="/form-elements.html" >
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
                                                            <a class="nav-link dropdown-toggle"  data-bs-toggle="dropdown"
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
                                                                        <a class="dropdown-item" target-link="empty.html">
                                                                            Empty page
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="cookie-banner.html">
                                                                            Cookie banner
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="list-view.html">
                                                                            Chat
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="activity.html">
                                                                            Activity
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="gallery.html">
                                                                            Gallery
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="invoice.html">
                                                                            Invoice
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="search-results.html">
                                                                            Search results
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="pricing.html">
                                                                            Pricing cards
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="pricing-table.html">
                                                                            Pricing table
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="faq.html">
                                                                            FAQ
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="users.html">
                                                                            Users
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="license.html">
                                                                            License
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" target-link="logs.html">
                                                                            Logs
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="music.html">
                                                                            Music
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="photogrid.html">
                                                                            Photogrid
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="tasks.html">
                                                                            Tasks
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="uptime.html">
                                                                            Uptime monitor
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="widgets.html">
                                                                            Widgets
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="wizard.html">
                                                                            Wizard
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="settings.html">
                                                                            Settings
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="trial-ended.html">
                                                                            Trial ended
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="job-listing.html">
                                                                            Job listing
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="page-loader.html">
                                                                            Page loader
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="modal.html" >
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
                                                            <a class="nav-link dropdown-toggle"   data-bs-toggle="dropdown"
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
                                                                        <a class="dropdown-item" target-link="/?index=layout-horizontal">
                                                                            Horizontal
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-boxed">
                                                                            Boxed
                                                                            <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-vertical">
                                                                            Vertical
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-vertical-transparent">
                                                                            Vertical transparent
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-vertical-right">
                                                                            Right vertical
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-condensed">
                                                                            Condensed
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-combo">
                                                                            Combined
                                                                        </a>
                                                                    </div>
                                                                    <div class="dropdown-menu-column">
                                                                        <a class="dropdown-item" target-link="/?index=layout-navbar-dark">
                                                                            Navbar dark
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-navbar-sticky">
                                                                            Navbar sticky
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-navbar-overlap">
                                                                            Navbar overlap
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-rtl">
                                                                            RTL mode
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-fluid">
                                                                            Fluid
                                                                        </a>
                                                                        <a class="dropdown-item" target-link="/?index=layout-fluid-vertical">
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
                                                            <a class="nav-link" target-link="/icons.html" >
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
                                                  <a class="nav-link" target-link="emails.html" >
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
                                                  <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button" aria-expanded="false" >
                                                    <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/lifebuoy -->
                                                      <svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 12m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0" /><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0" /><path d="M15 15l3.35 3.35" /><path d="M9 15l-3.35 3.35" /><path d="M5.65 5.65l3.35 3.35" /><path d="M18.35 5.65l-3.35 3.35" /></svg>
                                                    </span>
                                                    <span class="nav-link-title">
                                                      Help
                                                    </span>
                                                  </a>
                                                  <div class="dropdown-menu">
                                                    <a class="dropdown-item" target-link="https://tabler.io/docs" target="_blank" rel="noopener">
                                                      Documentation
                                                    </a>
                                                    <a class="dropdown-item" target-link="changelog.html">
                                                      Changelog
                                                    </a>
                                                    <a class="dropdown-item" target-link="https://github.com/tabler/tabler" target="_blank" rel="noopener">
                                                      Source code
                                                    </a>
                                                    <a class="dropdown-item text-pink" target-link="https://github.com/sponsors/codecalm" target="_blank" rel="noopener">
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

    @RequestMapping(value = "/menu/tree/sys", method = {RequestMethod.GET, RequestMethod.POST})
    public Response menuTreeSys() {
        MenuDTO Video = new MenuDTO()
                .setCode("Video")
                .setName("Video")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/file/list-view.html" >
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
                .setChildren(Arrays.asList());


        MenuDTO Database = new MenuDTO()
                .setCode("Database")
                .setName("Database")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/database/list-view.html" >
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
                .setChildren(Arrays.asList());
        MenuDTO file = new MenuDTO()
                .setCode("File")
                .setName("File")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/file/list-view.html" >
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
                .setChildren(Arrays.asList());
        MenuDTO photo = new MenuDTO()
                .setCode("Photo")
                .setName("Photo")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/photo/list-view.html" >
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
                .setChildren(Arrays.asList());
        MenuDTO Gallery = new MenuDTO()
                .setCode("Gallery")
                .setName("Gallery")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/gallery/list-view.html" >
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
                .setChildren(Arrays.asList());
        MenuDTO book = new MenuDTO()
                .setCode("Book")
                .setName("Book")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/book/list-view.html" >
                                <span class="nav-link-icon d-md-none d-lg-inline-block">
                                    <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-book"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 19a9 9 0 0 1 9 0a9 9 0 0 1 9 0" /><path d="M3 6a9 9 0 0 1 9 0a9 9 0 0 1 9 0" /><path d="M3 6l0 13" /><path d="M12 6l0 13" /><path d="M21 6l0 13" /></svg>   
                                </span>
                                <span class="nav-link-title">Book</span>
                            </a>
                        </li>
                        """)
                .setLinkUrl("")
                .setChildren(Arrays.asList());
        MenuDTO tool = new MenuDTO()
                .setCode("tool")
                .setName("tool")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/tool/view.html" >
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-box"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5" /><path d="M12 12l8 -4.5" /><path d="M12 12l0 9" /><path d="M12 12l-8 -4.5" /></svg>
                                </span>
                                <span class="nav-link-title">Tool</span>
                            </a>
                        </li>
                        """)
                .setChildren(Arrays.asList());
        MenuDTO ai = new MenuDTO()
                .setCode("AI")
                .setName("AI")
                .setIcon("""
                        """)
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/ai/list-view.html" >
                                <span class="nav-link-icon d-md-none d-lg-inline-block">
                                <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-ai"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M8 16v-6a2 2 0 1 1 4 0v6" /><path d="M8 13h4" /><path d="M16 8v8" /></svg>
                                </span>
                                <span class="nav-link-title">AI</span>
                            </a>
                        </li>
                        """)
                .setLinkUrl("/ai.html")
                .setChildren(Arrays.asList());
        List<MenuDTO> list = Arrays.asList(
                Database,
//                file,
//                Video,
//                photo,
                Gallery,
                book,
                tool,
                ai

        );
        return Response.success(list);
    }
}
