package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import java.util.ArrayList;
import java.util.List;

public class Seeder {

    public static void seed(FirstAidDao dao) {
        List<FirstAidTopic> topics = new ArrayList<>();
        topics.add(new FirstAidTopic("cpr_adult", "CPR (Adult)", "Hands-only compressions for an unresponsive adult.", 0, "cpr,heart,unresponsive,not breathing,cardiac"));
        topics.add(new FirstAidTopic("cpr_child", "CPR (Child/Infant)", "Compressions and rescue breaths for a child or infant.", 0, "cpr,baby,infant,child,kid"));
        topics.add(new FirstAidTopic("choking_adult", "Choking (Adult)", "Heimlich and back blows for an adult or older child.", 0, "choke,heimlich,airway"));
        topics.add(new FirstAidTopic("choking_infant", "Choking (Infant)", "Back blows and chest thrusts for a baby.", 0, "choke,baby,infant"));
        topics.add(new FirstAidTopic("severe_bleeding", "Severe Bleeding", "Stop heavy bleeding with direct pressure.", 0, "bleed,blood,wound,cut,hemorrhage"));
        topics.add(new FirstAidTopic("heart_attack", "Heart Attack", "Recognize and respond to a heart attack.", 0, "heart attack,chest pain,cardiac"));
        topics.add(new FirstAidTopic("stroke", "Stroke (FAST)", "Use FAST to recognize stroke and act fast.", 0, "stroke,fast,brain,face droop"));
        topics.add(new FirstAidTopic("anaphylaxis", "Anaphylaxis", "Severe allergic reaction, EpiPen and 911.", 0, "allergy,anaphylaxis,epipen,swelling"));
        topics.add(new FirstAidTopic("drowning", "Drowning", "Get out of water and start rescue breaths.", 0, "drown,water,submerged"));
        topics.add(new FirstAidTopic("seizure", "Seizure", "Keep a person having a seizure safe.", 1, "seizure,convulsion,fit,epilepsy"));
        topics.add(new FirstAidTopic("burns", "Burns", "Cool a burn with running water.", 1, "burn,scald,fire,heat"));
        topics.add(new FirstAidTopic("fracture", "Suspected Fracture", "Immobilize a possible broken bone.", 1, "fracture,broken,sprain"));
        topics.add(new FirstAidTopic("head_injury", "Head Injury", "Watch for concussion signs.", 1, "head,concussion,brain,bump"));
        topics.add(new FirstAidTopic("shock", "Shock", "Recognize and treat shock.", 1, "shock,pale,clammy,faint"));
        topics.add(new FirstAidTopic("asthma", "Asthma Attack", "Help someone use their inhaler.", 1, "asthma,wheeze,inhaler,breathing"));
        topics.add(new FirstAidTopic("heat_stroke", "Heat Stroke", "Cool a victim of heat stroke immediately.", 1, "heat,heatstroke,hot,sun"));
        topics.add(new FirstAidTopic("hypothermia", "Hypothermia", "Warm a dangerously cold person.", 1, "cold,hypothermia,frostbite"));
        topics.add(new FirstAidTopic("poisoning", "Poisoning", "Call Poison Control - do NOT induce vomiting.", 1, "poison,swallowed,toxic,chemical"));
        topics.add(new FirstAidTopic("snake_bite", "Snake Bite", "Keep still and call 911.", 1, "snake,bite,venom"));
        topics.add(new FirstAidTopic("electric_shock", "Electric Shock", "Cut the power before touching the victim.", 1, "electric,shock,electrocution,wire"));
        dao.insertTopics(topics);

        List<FirstAidStep> steps = new ArrayList<>();
        addSteps(steps, "cpr_adult", new String[][]{
                {"Check the scene is safe. Tap the person's shoulder and shout 'Are you OK?'", null},
                {"If unresponsive, call 911 immediately or have someone else call. Ask for an AED.", null},
                {"Place the person on their back on a firm surface. Tilt head back to open airway.", null},
                {"Place the heel of one hand in the center of the chest, other hand on top, fingers interlocked.", null},
                {"Push hard and fast: 2 inches deep, 100-120 per minute.", "Let chest fully recoil between compressions."},
                {"Continue until they respond, an AED arrives, or paramedics take over.", "Do NOT stop unless you physically cannot continue."}
        });
        addSteps(steps, "cpr_child", new String[][]{
                {"Check responsiveness. For infant, tap the foot. For child, tap the shoulder.", null},
                {"Have someone call 911. If alone, do 2 minutes of CPR before leaving to call.", null},
                {"Child: one hand for compressions, 2 inches deep. Infant: 2 fingers, 1.5 inches deep.", null},
                {"Give 30 compressions at 100-120/min, then 2 gentle rescue breaths.", "Cover both mouth and nose for infant."},
                {"Continue 30:2 cycles until help arrives or the child responds.", null}
        });
        addSteps(steps, "choking_adult", new String[][]{
                {"Ask 'Are you choking?' If they can't speak, cough, or breathe, act now.", null},
                {"Stand behind them. Lean them forward. Give 5 firm back blows between the shoulder blades.", null},
                {"If not dislodged, do 5 abdominal thrusts (Heimlich) above the navel, inward and upward.", null},
                {"Alternate 5 back blows and 5 abdominal thrusts until cleared.", null},
                {"If they become unresponsive, lower to the ground, call 911, and begin CPR.", "Check mouth for the object before rescue breaths."}
        });
        addSteps(steps, "choking_infant", new String[][]{
                {"Hold the infant face-down along your forearm, supporting head and jaw.", null},
                {"Give 5 firm back blows between the shoulder blades.", null},
                {"Turn face-up. 5 chest thrusts with 2 fingers in the center of chest, below nipple line.", null},
                {"Alternate back blows and chest thrusts until cleared or infant becomes unresponsive.", "Never do abdominal thrusts on infant under 1 year."},
                {"If infant becomes unresponsive, call 911 and start infant CPR.", null}
        });
        addSteps(steps, "severe_bleeding", new String[][]{
                {"Call 911 for any wound that spurts blood, won't stop, or is deeper than skin.", null},
                {"Apply firm, direct pressure with a clean cloth or gauze.", null},
                {"Don't lift the cloth. If blood soaks through, add more on top.", "Lifting the cloth disrupts the clot."},
                {"If possible, raise the wound above the heart.", null},
                {"If bleeding from a limb won't stop, apply a tourniquet 2-3 inches above the wound. Note the time.", "Tourniquets are for life-threatening bleeding only."},
                {"Keep them warm and still until paramedics arrive.", null}
        });
        addSteps(steps, "heart_attack", new String[][]{
                {"Signs: chest pressure, pain spreading to arm/jaw/back, shortness of breath, nausea, cold sweat.", null},
                {"Call 911 immediately. Do not drive yourself.", null},
                {"Have them sit down, lean back, stay calm. Loosen tight clothing.", null},
                {"If not allergic, give one adult aspirin (325 mg) to chew.", "Skip aspirin if unsure."},
                {"If they have prescribed nitroglycerin, help them take it.", null},
                {"If unresponsive and not breathing, begin CPR.", null}
        });
        addSteps(steps, "stroke", new String[][]{
                {"FAST: Face drooping, Arm weakness, Speech difficulty, Time to call 911.", null},
                {"Ask them to smile, raise both arms, repeat a simple sentence. Check for any failure.", null},
                {"Call 911 immediately. Note the exact time symptoms started.", "Time of onset is critical."},
                {"No food, water, or medication - they may not swallow safely.", null},
                {"If unresponsive but breathing, place in recovery position.", null}
        });
        addSteps(steps, "anaphylaxis", new String[][]{
                {"Signs: swelling of face/lips/tongue, trouble breathing, hives, weakness after a trigger.", null},
                {"If they have an EpiPen, help use it on the outer thigh. Hold 3 seconds.", null},
                {"Call 911 even if symptoms improve. They can return.", null},
                {"Lay flat with legs raised, unless trouble breathing - then sit up.", null},
                {"A second EpiPen can be given after 5-15 minutes if no improvement.", null},
                {"If unresponsive and not breathing, begin CPR.", null}
        });
        addSteps(steps, "drowning", new String[][]{
                {"Get them out of the water safely. Don't put yourself in danger.", null},
                {"If not breathing normally, call 911 and start CPR.", null},
                {"For drowning, give 2 rescue breaths first, then 30:2 cycles.", null},
                {"Even if they seem fine, get medical care - 'dry drowning' can happen hours later.", null},
                {"Keep warm to prevent hypothermia.", null}
        });
        addSteps(steps, "seizure", new String[][]{
                {"Stay calm. Don't hold them down or stop movements.", null},
                {"Move sharp objects away. Cushion their head.", null},
                {"Time the seizure. Loosen anything tight around the neck.", null},
                {"Once still, gently roll onto their side (recovery position).", null},
                {"Stay with them until fully alert.", null},
                {"Call 911 if: seizure lasts over 5 min, another follows, injured, pregnant, or first time.", "Never put anything in their mouth."}
        });
        addSteps(steps, "burns", new String[][]{
                {"Move away from the heat source. Clothing on fire: stop, drop, roll.", null},
                {"Run cool (not cold) water over the burn for at least 20 minutes.", "Do NOT use ice."},
                {"Remove jewelry and loose clothing near the burn. Don't pull off anything stuck.", null},
                {"Cover loosely with a clean non-stick dressing or plastic wrap.", null},
                {"No butter, oil, toothpaste, or ointments.", "They trap heat and raise infection risk."},
                {"Call 911 for burns larger than a palm, on face/hands/genitals/joints, or white/charred/leathery.", null}
        });
        addSteps(steps, "fracture", new String[][]{
                {"Don't move them unless in danger. Call 911 for deformity, bone showing, or spine injury.", null},
                {"Keep the injury still. Support in the position you found it.", null},
                {"Apply a cold pack wrapped in cloth. 20 min on, 20 min off.", "Never ice directly on skin."},
                {"If you must move them, splint past the joints above and below the injury.", null},
                {"Watch for shock signs: pale, rapid breathing, weakness. Keep warm.", null}
        });
        addSteps(steps, "head_injury", new String[][]{
                {"Call 911 for: unconsciousness, repeated vomiting, severe headache, confusion, unequal pupils, seizure, or clear fluid from nose/ears.", null},
                {"Keep still. Assume a neck injury - don't move head or neck.", null},
                {"For bleeding, apply gentle pressure. Don't press hard if skull fracture suspected.", null},
                {"Apply a cold pack to bumps to reduce swelling.", null},
                {"Watch 24-48 hours for: worsening headache, confusion, drowsiness, vomiting, behavior change.", "Wake them every 2 hours the first night."}
        });
        addSteps(steps, "shock", new String[][]{
                {"Signs: pale/cool/clammy skin, rapid weak pulse, rapid shallow breathing, confusion.", null},
                {"Call 911. Shock is life-threatening.", null},
                {"Lay flat on back. Raise legs 12 inches if no head/neck/spine injury.", null},
                {"Loosen tight clothing. Cover with a blanket.", null},
                {"No food or drink.", null},
                {"If unresponsive and not breathing, begin CPR.", null}
        });
        addSteps(steps, "asthma", new String[][]{
                {"Help them sit upright. Loosen tight clothing.", null},
                {"Help use their reliever inhaler (blue). 1 puff every 30-60 seconds, up to 10.", null},
                {"Stay calm. Encourage slow, steady breathing.", null},
                {"Call 911 if: inhaler doesn't help, can't speak full sentences, lips blue, or no inhaler.", null},
                {"Repeat doses every 10 minutes while waiting.", null}
        });
        addSteps(steps, "heat_stroke", new String[][]{
                {"Signs: very high body temp, hot/red/dry or damp skin, fast pulse, headache, confusion.", null},
                {"Call 911 - heat stroke is an emergency.", null},
                {"Move to a cool place. Remove excess clothing.", null},
                {"Cool rapidly: water on skin, fan, ice packs at neck/armpits/groin, or immerse in cool water.", null},
                {"No drinks if confused or unresponsive.", null}
        });
        addSteps(steps, "hypothermia", new String[][]{
                {"Signs: shivering, confusion, slurred speech, drowsiness, weak pulse, shallow breathing.", null},
                {"Call 911. Move gently to a warm place - rough handling can trigger cardiac arrest.", null},
                {"Remove wet clothing. Replace with dry warm layers. Wrap in blankets including head.", null},
                {"If alert, give warm sweet drinks. No alcohol or caffeine.", null},
                {"Warm (not hot) compresses on chest, neck, groin. Don't warm arms and legs first.", "Warming limbs first can shock the heart."}
        });
        addSteps(steps, "poisoning", new String[][]{
                {"Call Poison Control: 1-800-222-1222. Have the container ready.", null},
                {"If unresponsive, not breathing, or seizing, call 911 first.", null},
                {"Do NOT induce vomiting unless Poison Control tells you to.", "Many substances damage more coming back up."},
                {"Skin contact: rinse 15-20 min with running water.", null},
                {"Eye contact: flush with lukewarm water 15-20 min.", null},
                {"Inhaled: fresh air immediately.", null}
        });
        addSteps(steps, "snake_bite", new String[][]{
                {"Move away from the snake. Remember color/pattern - don't catch it.", null},
                {"Call 911. Keep them calm and still to slow venom spread.", null},
                {"Keep the bite below heart level. Remove rings/watches before swelling.", null},
                {"Do NOT cut, suck venom, ice, tourniquet, or give alcohol.", "These are dangerous and ineffective."},
                {"Mark the edge of swelling with a pen and note the time.", null}
        });
        addSteps(steps, "electric_shock", new String[][]{
                {"Do NOT touch them if still in contact with the source.", null},
                {"Cut the power at the breaker, or use a dry non-conductive object to push the source away.", null},
                {"Once safe, call 911. Electric shock can cause delayed cardiac problems.", null},
                {"If not breathing normally, begin CPR.", null},
                {"Cover burns with clean cloth. Don't move if fall injury is suspected.", null}
        });

        dao.insertSteps(steps);
    }

    private static void addSteps(List<FirstAidStep> list, String topicId, String[][] entries) {
        for (int i = 0; i < entries.length; i++) {
            list.add(new FirstAidStep(topicId, i + 1,
                    entries[i][0], entries[i].length > 1 ? entries[i][1] : null));
        }
    }
}