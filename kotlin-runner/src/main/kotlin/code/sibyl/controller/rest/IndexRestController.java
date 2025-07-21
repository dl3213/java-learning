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
        MenuDTO Layout = new MenuDTO()
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
        list.add(home);
        list.add(Layout);
        list.addAll(getCustomizeMenu());
        return Response.success(list);
    }

    public List<MenuDTO> getCustomizeMenu() {
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
                .setCode("File")
                .setName("File")
                .setHtml("""
                        <li class="nav-item">
                                                    <a class="nav-link" target-link="/templates/file/list-view.html" >
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
        return Arrays.asList(
                Gallery,
                book,
                music,
                Database,
                tool,
                ai
        );
    }
}
