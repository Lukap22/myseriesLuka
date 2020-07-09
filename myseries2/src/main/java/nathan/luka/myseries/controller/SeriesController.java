package nathan.luka.myseries.controller;

import nathan.luka.myseries.dataprovider.DataProvider;
import nathan.luka.myseries.model.Review;
import nathan.luka.myseries.model.Serie;
import nathan.luka.myseries.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class SeriesController {
    private final DataProvider model = DataProvider.getInstance();
    private HttpSession httpSession;
    private int season;
    private int episode;
    private boolean watched;
    private int id;

    //load all series

    @GetMapping("/series")
    public String series(Model model, HttpSession httpSession) {
        if (!isLoggedIn(httpSession)) {
            model.addAttribute("series", this.model.getSeries());
            return "series";
        }
        model.addAttribute("series", this.model.getSeries());
        return "series";
    }

    @GetMapping("/sorteer")
    public String sorteerSeries(Model model, HttpSession httpSession) {

        model.addAttribute("series", this.model.insertionSort(this.model.getSeries()));
        return "series";


//        User user1 = this.model.getUserByUsername((String) httpSession.getAttribute("username"));
//        model.addAttribute("series", this.model.insertionSort(this.model.getSeriesfromuser(user1)));
//        return "/eigenseries";
    }
// TODO: 15/06/2020 sorteer 
//    @GetMapping("/zoekFunctie")
//    public String zoek(Model model, HttpSession httpSession, String seriestring){
//        if (!isLoggedIn(httpSession)) {
//            return "/login";
//        }
//        model.addAttribute("serie", this.model.findSerieByTitle(seriestring));
//        return "/serie";
//    }

    @GetMapping("/eigenseries")
    public String getEigenSeries(Model model, HttpSession httpSession) {
        if (!isLoggedIn(httpSession)) {
            return "redirect:/login";
        }

        User user1 = this.model.getUserByUsername((String) httpSession.getAttribute("username"));
        if (user1 == null) {
            return "/login";
        }
        model.addAttribute("eigen_series", this.model.getSeriesfromuser(user1));
        return "eigenseries";
    }

    @GetMapping("/backup")
    public String backupSeries() {
//        if (!isLoggedIn(httpSession)) {
//            return "redirect:/login";
//        }

        DataProvider.getDataProvider().backupSeriesToJsonFile();
        System.out.println("tried to backup series");
        return "redirect:/";
    }

    @GetMapping("/")
    public String homeView(Model model) {
        model.addAttribute("home_page", this.model.getSeries());
        return "index";
    }

    // TODO: 14/06/2020 series voor solo user
    @GetMapping("/series_solo")
    public String soloSeries(Model model, HttpSession httpSession) {
        if (isLoggedIn(httpSession)){
            model.addAttribute("series",   model.addAttribute("series", this.model.getUserByUsername(String.valueOf(httpSession.getAttribute("username"))).getSerie()));
            return "series";
        }
        return "redirect:login";
    }

    //https://github.com/NathanGanesh/myseries/blob/master/src/main/java/nathan/luka/myseries/controller/SeriesController.java
    @PostMapping("/series")
    public String addSerie(@ModelAttribute(value = "perie") Serie serie, HttpSession httpSession) {
        if (!isLoggedIn(httpSession)) {
            return "redirect:/login";
        }
        User user1 = model.getUserByUsername((String) httpSession.getAttribute("username"));
        if (user1 != null) {
            serie.setUser(user1);
            serie.setTitle(serie.getTitle());
            model.addSerie(serie);
            return "redirect:/eigenseries";
        }
        return "series";
    }

    //get a specific serie

    @GetMapping("/serie/{id}/{season}")
    public String getSerie(@PathVariable("id") int id, Model model, HttpSession httpSession, @PathVariable("season")  int season) {
        if (!isLoggedIn(httpSession)) {
            return "redirect:/login";
        }
        Serie serie = this.model.getSerieById(id);
        if (serie != null) {
            model.addAttribute("serie", serie);
            model.addAttribute("user", this.model.getUserByUsername((String) httpSession.getAttribute("username")));
            return "serie";
        }
        return null;
    }
//    @GetMapping(path = "/delete/{id}")
//    public String deleteTournament(@PathVariable int id, RedirectAttributes redirectAttributes){
//        DataProvider.deleteTournament(id);
//        redirectAttributes.addFlashAttribute("response", "Toernooi verwijderd!");
//        redirectAttributes.addFlashAttribute("responseClass", "response-success");
//        return "redirect:/overview";
//    }


    @PostMapping("/serie/{id}/{season}/{episode}")
    public RedirectView setEpisodeWatched(@PathVariable int id, @PathVariable int season, @PathVariable int episode, HttpSession httpSession ) {
        User user = model.getUserByUsername((String) httpSession.getAttribute("username"));
        Serie serie = model.getSerieById(id);
//        @RequestParam("episode") int episode
        System.out.println("postMapping setEpisodeWatched");

        user.setEpisodeWatched(serie.getThemoviedbSerieID(), season, episode);
        return new RedirectView("/serie/" + id + "/" + season);
    }
    @GetMapping("/serie/{id}/{season}/{episode}")
    public RedirectView setEpisodeWatched2(@PathVariable int id, @PathVariable int season, @PathVariable int episode, HttpSession httpSession ) {
        User user = model.getUserByUsername((String) httpSession.getAttribute("username"));
        Serie serie = model.getSerieById(id);
//        @RequestParam("episode") int episode
        System.out.println("getMapping setEpisodeWatched");


        user.setEpisodeWatched(serie.getThemoviedbSerieID(), season, episode);
        return new RedirectView("/serie/" + id + "/" + season);
//        return "redirect: /serie/" + id + "/" + season;
    }

    @PostMapping("/serie/delete/{id}")
    public RedirectView removeSerieTitle(@PathVariable("id") int id, HttpSession httpSession) {
        if (!isLoggedIn(httpSession)) {
            return new RedirectView("/login");
        }
        Serie serie = this.model.getSerieById(id);
        if (serie != null) {
            this.model.getSeries().remove(serie);
        }
        return new RedirectView("/series");
    }

    // TODO: 14/06/2020 niet werkend
    @PostMapping("/serie/update/{id}")
    public RedirectView setSerieTitle(@PathVariable("id") int id, String updated_name, HttpSession httpSession) {
        if (!isLoggedIn(httpSession)) {
            return new RedirectView("/login");
        }
        Serie serie = this.model.getSerieById(id);
        if (serie != null) {
            serie.setTitle(updated_name);
        }
        return new RedirectView("/series");
    }


    //add review

    @PostMapping("/series/{id}")
    public RedirectView addReviewToSerie(@PathVariable("id") int id,
                                         @RequestBody Review review,
                                         @RequestParam("user") String username) {
        if (model.hasUserWithUsername(username)) {
            Serie serie = model.getSerieById(id);
            if (serie != null) {
                review.setUser(model.getUserByUsername(username));
                serie.addReview(review);
                return new RedirectView("/serie");
            } else {
                return new RedirectView("/series");
            }
        } else {
            return new RedirectView("/series");
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return (session.getAttribute("username") != null);
    }


}
