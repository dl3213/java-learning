package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.model.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/rest/v1")
@RequiredArgsConstructor
public class IndexRestController {

    @RequestMapping(value = "/menu/tree/base", method = {RequestMethod.GET, RequestMethod.POST})
    public Response menuTreeBase() {
        ArrayList<MenuDTO> list = new ArrayList<>();
        MenuDTO home = new MenuDTO()
                .setCode("Home")
                .setName("Home")
                .setIsActive(true)
                .setHtml("""
                        <li class="nav-item active">
                                                    <a class="nav-link"  target-link="/" target-link="/" >
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
                .setChildren(Arrays.asList());

        MenuDTO form = new MenuDTO()
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
                .setChildren(Arrays.asList());

        MenuDTO Layout = new MenuDTO()
                .setCode("Layout")
                .setName("Layout")
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
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-horizontal.html">
                                                                    Horizontal
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-boxed.html">
                                                                    Boxed
                                                                    <span class="badge badge-sm bg-green-lt text-uppercase ms-auto">New</span>
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-vertical.html">
                                                                    Vertical
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-vertical-transparent.html">
                                                                    Vertical transparent
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-vertical-right.html">
                                                                    Right vertical
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-condensed.html">
                                                                    Condensed
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-combo.html">
                                                                    Combined
                                                                </a>
                                                            </div>
                                                            <div class="dropdown-menu-column">
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-rtl.html">
                                                                    RTL mode
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-fluid.html">
                                                                    Fluid
                                                                </a>
                                                                <a class="dropdown-item" target-link="" layout-link="/layout-fluid-vertical.html">
                                                                    Fluid vertical
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                        """)
                .setLinkUrl("")
                .setChildrenLine(2)
                .setChildren(Arrays.asList());

        MenuDTO Interface = new MenuDTO()
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
                                                                <a class="dropdown-item" target-link="/placeholder.html" href="#">
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
                .setChildrenLine(2);


        list.add(home);
        list.add(Interface);
        list.add(form);
        list.add(Layout);
        list.addAll(getCustomizeMenu());
        return Response.success(list);
    }

    public List<MenuDTO> getCustomizeMenu() {

        MenuDTO Box = new MenuDTO()
                .setCode("Box")
                .setName("Box")
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/box/index-view.html" >
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                    <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-box"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5" /><path d="M12 12l8 -4.5" /><path d="M12 12l0 9" /><path d="M12 12l-8 -4.5" /></svg>
                                </span>
                                <span class="nav-link-title">Box</span>
                            </a>
                        </li>
                        """)
                .setChildren(Arrays.asList());

        MenuDTO music = new MenuDTO()
                .setCode("Music")
                .setName("Music")
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/music/list-view.html" >
                                <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                    <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-music-plus"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 17a3 3 0 1 0 6 0a3 3 0 0 0 -6 0" /><path d="M9 17v-13h10v8" /><path d="M9 8h10" /><path d="M16 19h6" /><path d="M19 16v6" /></svg>
                                </span>
                                <span class="nav-link-title">Music</span>
                            </a>
                        </li>
                        """)
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

        MenuDTO photo = new MenuDTO()
                .setCode("Photo")
                .setName("Photo")
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
        MenuDTO File = new MenuDTO()
                .setCode("File")
                .setName("File")
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/file/list-view.html" >
                                            <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                              <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-file"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M14 3v4a1 1 0 0 0 1 1h4" /><path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z" /></svg>
                                            </span>
                                                        <span class="nav-link-title">File</span>
                                                    </a>
                                                </li>
                        """)
                .setLinkUrl("/gallery.html")
                .setChildren(Arrays.asList());
        MenuDTO pixiv = new MenuDTO()
                .setCode("Pixiv")
                .setName("Pixiv")
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/pixiv/list-view.html" >
                                            <span class="nav-link-icon d-md-none d-lg-inline-block"><!-- Download SVG icon from http://tabler-icons.io/i/mail-opened -->
                                              <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-file"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M14 3v4a1 1 0 0 0 1 1h4" /><path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z" /></svg>
                                            </span>
                                                        <span class="nav-link-title">Pixiv</span>
                                                    </a>
                                                </li>
                        """)
                .setChildren(Arrays.asList());
        MenuDTO book = new MenuDTO()
                .setCode("Book")
                .setName("Book")
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
                .setChildren(Arrays.asList());
        MenuDTO test = new MenuDTO()
                .setCode("Test")
                .setName("Test")
                .setHtml("""
                        <li class="nav-item">
                            <a class="nav-link" target-link="/templates/test/index.html" >
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-tabler icons-tabler-outline icon-tabler-question-mark"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M8 8a3.5 3 0 0 1 3.5 -3h1a3.5 3 0 0 1 3.5 3a3 3 0 0 1 -2 3a3 4 0 0 0 -2 4" /><path d="M12 19l0 .01" /></svg>
                                <span class="nav-link-title">Test</span>
                            </a>
                        </li>
                        """)
                .setChildren(Arrays.asList());
        return Arrays.asList(
                File,
                pixiv,
                book,
                Box,
                music,
                Database,
                tool,
                test
//                , ai
        );
    }
}
