package net.rkr1410.playground.list;

import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.thing.ThingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

    private final ThingRepository thingRepository;

    public ListController(ThingRepository thingRepository) {
        this.thingRepository = thingRepository;
    }

    @GetMapping("/list")
    public String getList(Model model) {
        Thing list = thingRepository.getListsWithChildren().iterator().next();

        model.addAttribute("list", list);
        return "list/list";
    }
}
