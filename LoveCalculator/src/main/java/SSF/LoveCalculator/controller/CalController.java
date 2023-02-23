package SSF.LoveCalculator.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import SSF.LoveCalculator.model.Compatibility;
import SSF.LoveCalculator.service.CalService;

@Controller
@RequestMapping("/compatibility")
public class CalController {

    @Autowired
    private CalService calSvc;

    @GetMapping
    public String getCompatibility(@RequestParam(required = true) String fname,
            @RequestParam(required = true) String sname,
            Model model)
            throws IOException {
        Optional<Compatibility> r = this.calSvc.calCompatibility(fname, sname);
        model.addAttribute("compatibility", r.get());
        return "result";
    }

    @GetMapping(path = "/list")
    public String getAllMatches(Model model) throws IOException {
        Compatibility[] matchArr = calSvc.getAllMatches();
        model.addAttribute("arr", matchArr);
        return "list";
    }
    
}