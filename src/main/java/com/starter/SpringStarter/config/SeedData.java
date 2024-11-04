package com.starter.SpringStarter.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.starter.SpringStarter.models.Account;
import com.starter.SpringStarter.models.Authority;
import com.starter.SpringStarter.models.Post;
import com.starter.SpringStarter.services.AccountService;
import com.starter.SpringStarter.services.AuthorityService;
import com.starter.SpringStarter.services.PostService;
import com.starter.SpringStarter.util.constants.Privileges;
import com.starter.SpringStarter.util.constants.Roles;

// We will be populating the database with some initial data

@Component
public class SeedData implements CommandLineRunner { // Implements CommandLineRunner, so the run method will be
    // executed automatically when the Spring Boot application starts.

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for (Privileges auth : Privileges.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getPrivilege());
            authorityService.save(authority);
        }

        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("user@user.com");
        account01.setPassword("password");
        account01.setFirstname("user");
        account01.setLastname("lastname");
        account01.setAge(16);
        account01.setDate_of_birth(LocalDate.parse("2003-12-23"));
        account01.setGender("Male");

        account02.setEmail("akbagheltdl@gmail.com");
        account02.setPassword("password");
        account02.setFirstname("admin");
        account02.setLastname("lastname");
        account02.setRole(Roles.ADMIN.getRole());
        account02.setAge(18);
        account02.setDate_of_birth(LocalDate.parse("2003-11-27"));
        account02.setGender("Female");

        account03.setEmail("editor@editor.com");
        account03.setPassword("password");
        account03.setFirstname("editor");
        account03.setLastname("lastname");
        account03.setRole(Roles.EDITOR.getRole());
        account03.setAge(17);
        account03.setDate_of_birth(LocalDate.parse("2003-10-13"));
        account03.setGender("Male");

        account04.setEmail("super_editor@editor.com");
        account04.setPassword("password");
        account04.setFirstname("super_editor");
        account04.setLastname("lastname");
        account04.setRole(Roles.EDITOR.getRole());
        account04.setAge(19);
        account04.setDate_of_birth(LocalDate.parse("2003-04-29"));
        account04.setGender("Female");
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privileges.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        authorityService.findById(Privileges.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        List<Post> posts = postService.getAll();
        if (posts.size() == 0) {
            Post post01 = new Post();
            post01.setTitle("Pikachu");
            post01.setBody(
                    """
                            <img src="https://wallpapers.com/images/high/pikachu-in-the-forest-76sdhn2b9fen4mzl.webp" />

                            <br/> <p> Pikachu (Japanese: ピカチュウ Pikachu) is an Electric-type Pokémon introduced in Generation I.
                            It evolves from Pichu when leveled up with high friendship and evolves into Raichu when exposed to a Thunder Stone.
                            In Alola, Pikachu evolves into Alolan Raichu when exposed to a Thunder Stone.
                            Pikachu has sixteen alternate forms that fall into four groups: Cosplay Pikachu, Pikachu in a cap, the partner Pikachu, and Gigantamax Pikachu. Ordinary Pikachu can Gigantamax into Gigantamax Pikachu if it has the Gigantamax Factor. Additionally, many other Pikachu variants have appeared in various media.
                            Cosplay Pikachu, Pikachu in a cap, the partner Pikachu, and Pikachu with the Gigantamax Factor cannot evolve. The Pikachu received at the beginning of Pokémon Yellow will refuse to evolve into Raichu unless it is traded and evolved on another save file.
                            Pikachu is popularly known as the mascot of the Pokémon franchise and one of Nintendo's major mascots. It is also the game mascot of and the player's first Pokémon in Pokémon Yellow and Let's Go, Pikachu!, the player's first Pokémon in Pokémon Rumble Blast and Pokémon Rumble World, and has made numerous appearances on the covers of spin-off games. </p>
                            """);
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Dr. Strange");
            post02.setBody(
                    """

                                        <img src="https://wallpapers.com/images/high/doctor-strange-power-5wj6u97w6yyx7fdk.webp"/>
                                       <br/> <p>Doctor Stephen Vincent Strange, M.D., Ph.D. is a Master of the Mystic Arts. Originally a brilliant but arrogant neurosurgeon, Strange got into a car accident which resulted in his hands becoming crippled. Once Western medicine failed him, Strange embarked on a journey to Kamar-Taj, where he was trained by the Ancient One in the ways of magic and the Multiverse. Although he focused on healing his hands, Strange was drawn into a conflict with Kaecilius and the Zealots, who were working for Dormammu and had sought to merge Earth with the Dark Dimension. Following the demise of the Ancient One and the defeat of Kaecilius, Strange then became the new protector of the Sanctum Sanctorum, seeking to defend the Earth from other inter-dimensional threats.

                            From his new position, Strange aided Thor in locating Odin, before he learned of Thanos' attempts to gain all of the Infinity Stones and cause a universal genocide. Since Strange was the protector of the Time Stone, he was attacked and captured by Thanos' Black Order, only for him to then be rescued by Iron Man and Spider-Man. Joining forces with the Guardians of the Galaxy, Strange battled against Thanos. However, as Strange had seen into the future and knew his only path to victory, he willingly handed over the Time Stone, allowing Thanos to wipe out half of life, including Strange. However, Strange and all the rest of Thanos' victims were resurrected five years later by the Avengers and battled against an alternate Thanos, ending in the Mad Titan's defeat.

                            Strange offered his aid to Spider-Man, whose identity of Peter Parker was unmasked by Mysterio, as he asked Strange to make the world forget this. However, due to Parker's interference, the spell went wrong, and Strange inadvertently unleashed enemies of Spider-Men from across the Multiverse. Capturing these enemies, Strange tried to use the Macchina di Kadavus to send them back to their universes to meet their inevitable fates, which Spider-Man had refused to allow, hoping to give these people a chance at redemption. Having been trapped in the Mirror Dimension during a duel with Spider-Man, Strange returned and fought to contain the Multiversal rift. With no other choice, Strange cast a spell to return the visitors back to their universes and close the rifts, but also at the cost of making everyone, including himself, forget Parker's existence.</p>

                                        """);
            post02.setAccount(account02);
            postService.save(post02);

            Post post03 = new Post();
            post03.setTitle("Toji Fushiguro");
            post03.setBody(
                    """
                            <img src="https://c4.wallpaperflare.com/wallpaper/95/627/603/jujutsu-kaisen-fushiguro-toji-weapon-sword-muscles-hd-wallpaper-preview.jpg" />

                            <br/> <p> Toji Fushiguro (伏ふし黒ぐろ甚とう爾じ Fushiguro Tōji?), born Toji Zenin (禪ぜん院いん甚とう爾じ Zen'in Tōji?) is a recurring character in the Jujutsu Kaisen series.
                            He was a former member of the Zenin clan and the infamous assassin known as the Sorcerer Killer (術じゅつ師し殺ごろし Jutsushi Goroshi?),
                            contracted by the Time Vessel Association among other groups throughout his time as a non-curse user.
                             He was also the father of Megumi Fushiguro and former enemy of Satoru Gojo. While working for the Star Religious Group, Toji serves as the primary antagonist of the Gojo's Past Arc.
                             As a non-sorcerer raised in the Zenin clan who values cursed techniques above all, Toji is a man who has suffered due to the interests of the sorcerer clans in a very similar manner to Maki Zenin.
                             His evil actions result from his dejection towards the jujutsu world as he attempts to spite it. Born without cursed energy, Toji eventually left the Zenin clan and married a woman,
                             taking her name "Fushiguro".[8] Her kindness helped Toji change his ways. He stopped gambling and hunting sorcerers; it was likely her influence that stopped Toji from taking revenge on the Zenin clan.
                             However, after her death, Toji reverted back to his old, cold, self.[1] He sold their son to the Zenin clan because he believed in young Megumi's potential to be a real sorcerer,[9] naming him "Megumi" because it means blessings.[10]</p>
                            """);
            post03.setAccount(account03);
            postService.save(post03);

            Post post04 = new Post();
            post04.setTitle("Iron-Man");
            post04.setBody(
                    """
                            <img src="https://c4.wallpaperflare.com/wallpaper/24/526/333/iron-man-marvel-comics-wallpaper-preview.jpg" />

                            <br/> <p> Iron Man is a superhero appearing in American comic books published by Marvel Comics. Co-created by writer and editor Stan Lee, developed by scripter Larry Lieber, 
                            and designed by artists Don Heck and Jack Kirby, the character first appeared in Tales of Suspense #39 in 1962 (cover dated March 1963) and received his own title with Iron Man #1 in 1968. 
                            Shortly after his creation, Iron Man became a founding member of the superhero team, the Avengers, alongside Thor, Ant-Man, the Wasp, and the Hulk. Iron Man stories, individually and with the Avengers, 
                            have been published consistently since the character's creation. Iron Man is the superhero persona of Anthony Edward "Tony" Stark, a businessman and engineer who runs the weapons manufacturing company Stark Industries. 
                            When Stark was captured in a war zone and sustained a severe heart wound, he built his Iron Man armor and escaped his captors. Iron Man's suits of armor grant him superhuman strength, flight, energy projection, and other abilities. 
                            The character was created in response to the Vietnam War as Lee's attempt to create a likeable pro-war character. Since his creation, Iron Man has been used to explore political themes, with early Iron Man stories being set in the Cold War. 
                            The character's role as a weapons manufacturer proved controversial, and Marvel moved away from geopolitics by the 1970s. Instead, the stories began exploring themes such as civil unrest, technological advancement, corporate espionage, alcoholism, and governmental authority.</p>
                            """);
            post04.setAccount(account04);
            postService.save(post04);
        }
    }
}

// This code is meant to ensure that your application starts with some
// predefined data in the database.