/*******************************************************************************
 * Copyright 2016 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

/**
 * Foot Problem Types and descriptions taken from: <a href=
 * "http://www.healthinaging.org/aging-and-health-a-to-z/topic:foot-problems/info
 * :causes-and-symptoms/ " >http://www.healthinaging.org/aging-and-health-a-to-z
 * /topic:foot-problems/info :causes-and-symptoms/ </a>
 * 
 * and
 * <a href="http://www.ipfh.org/foot-conditions/foot-conditions-a-z/" >http://
 * www.ipfh.org/foot-conditions/foot-conditions-a-z/</a>
 * 
 *
 * @author eandgrg
 */
public enum FootProblemType {

    /**
     * If you are obese, you may develop Achilles tendonitis from years of extra
     * stress on the tendon. It is also associated with an inherited shortened
     * Achilles tendon or from wearing high-heeled shoes. Sudden severe Achilles
     * tendonitis or even a ruptured Achilles tendon may occur as a side effect
     * of certain antibiotics (fluoroquinolones such as Levoquin or Cipro).
     */
    ACHILLES_TENDONITIS,

    /**
     * Flat feet and its opposite—an abnormally high arch (claw foot or hollow
     * foot)—are caused by an Achilles tendon (the vertical tendon behind the
     * ankle) that is either too tight or too loose. These conditions are
     * usually inherited, but flat feet may also develop after years of wearing
     * high-heeled shoes. In this case, the condition is known as posterior
     * tibial tendon dysfunction (PTTD). Flat feet are also linked to obesity,
     * diabetes, rheumatoid arthritis, or the use of steroids.
     */
    ARCH_PROBLEMS,

    /**
     * Osteoarthritis, gout, and rheumatoid arthritis are among the conditions
     * that can cause severe foot pain.
     */
    ARTHRITIC_FOOT,

    /**
     * Athleteʹs Foot (tinea pedis) is a fungal infection that develops in the
     * moist areas between the toes and in other areas of the foot. - See more
     * at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/athletes-foot/#
     * sthash.VgEtw8Az.dpuf
     */
    ATHLETES_FOOT,

    /**
     * Black toe is a relatively common condition among runners, vigorous
     * walkers, hikers, and participants in other sports and activities where
     * the feet are subjected to stress and strain, such as football,
     * basketball, baseball and soccer. The condition generally is the result of
     * impact trauma, especially in the big toe.
     * 
     * People who walk and run on hard surfaces such as concrete and asphalt are
     * more likely to get black toe than those who exercise on natural surfaces
     * or softer man-made surfaces. Hikers can experience black toe as a result
     * of the toes banging against the front area of their shoes or boots,
     * especially on downhill sections of trails. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/black-toe/#sthash
     * .PXw53F2F.dpuf
     */
    BLACK_TOE,

    /**
     * Nearly everyone gets blisters from time to time. Whether they form on the
     * hands from hard work, or whether they form on the feet from shoes or just
     * going barefoot, they're an aggravation and a potential gateway to more
     * serious conditions if left untreated. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/blisters/#sthash.
     * zTgwgSh7.dpuf
     */
    BLISTERS,

    /**
     * A bunion (hallux valgus) is a bony protrusion that forms most often in
     * the joint at the base of the big toe. The big toe joint enlarges and
     * forces the toe to point back toward the other toes, encroaching on them
     * and sometimes overlapping them. This abnormal positioning puts pressure
     * on the big toe joint, causing it to grow outward beyond the normal
     * profile of the foot, resulting in pain and fitting difficulties in normal
     * footwear. Bunions can also occur in the fifth toe joint (the little toe).
     * In this location they are called “tailor’s bunions” or “bunionettes.”
     * 
     * Bunions are one of the most common foot issues and occur much more
     * frequently in women than in men, mostly because women often wear high
     * heels and pointy shoes. This deformity of the foot may be an inherited
     * trait but may also result from many years of friction due to ill-fitting
     * footwear. Flat feet, gout, and arthritis also increase your chance of
     * developing a bunion.
     */
    BUNIONS,

    /**
     * Calluses are similar to corns, but develop on the ball or heel of your
     * foot.
     */
    CALLUSES,

    /**
     * Also called ‟Charcot arthropathy“ or ‟Charcot neurarthropathy,“ Charcot
     * foot is a condition that causes weakening of the bones in the foot. It
     * occurs in people who have moderate-to-severe neuropathy (nerve damage)
     * most commonly as a result of diabetes. The condition is serious. As it
     * progresses, the joints collapse, bones weaken and fracture and the foot
     * becomes deformed, creating areas of increased pressure and an increased
     * risk of ulceration, infection and eventually, amputation. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/charcot-foot-also
     * -called-charcot-arthropathy-or-charcot-neurarthropathy/#sthash.ygRusdgz.
     * dpuf
     */
    CHARCOT_FOOT,
    /**
     * Corns are caused by friction from poorly fitted shoes or socks or from
     * toes rubbing against each other.
     */
    CORNS,
    /**
     * Cracked heels, also called “heel fissures,” are a fairly common foot
     * condition. For many people they are merely a nuisance or a cosmetic
     * problem, but if the cracks are deep, they can be painful when you’re on
     * your feet. Cracked heels may also bleed.
     * 
     * Cracked heels generally are caused by dry skin (xerosis) and are more
     * difficult to treat if the skin around the rim of the heel is thickened or
     * callused. In severe cases, the cracks or fissures can become infected. -
     * See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/cracked-heels/#
     * sthash.ttfVBe85.dpuf
     */
    CRACKED_HEELS,
    DECREASED_ANKLE_FLEXIBILITY,
    DECREASED_PLANTAR_TACTILE_SENSITIVITY,
    DECREASED_TOE_PLANTAR_FLEXOR_STRENGTH,

    /**
     * Reduced sensation in feet from diabetes, making it hard to realize that
     * foot is injured. Also, blood flow in your feet is impaired in diabetes,
     * so infections can be harder to fight off.
     */
    DIABETIC_FOOT,
    
    DISABLING_FOOT_PAIN,
    
    /**
     * “Eczema” is used to describe several different types of allergic skin
     * conditions. Also called “dermatitis,” or “atopic dermatitis,” normally it
     * is not serious, but it can be uncomfortable and aggravating because it
     * makes the skin dry, inflamed and itchy.
     * 
     * The condition tends to manifest in children and adults where the skin
     * creases—on the ankles, wrists, neck, and behind the “hinge” joints such
     * as the inner elbow and knee. Although it is a chronic condition, symptoms
     * can be minimized by taking certain precautions. The National Eczema
     * Association estimates that more than 30 million people in the United
     * States have the condition.
     * 
     * Another skin condition, psoriasis, can look similar because both cause
     * rashes and itching. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/eczema/#sthash.
     * p4XE6liF.dpuf
     */
    ECZEMA,

    /**
     * Foot ulcers are open sores that develop on the feet, usually as a result
     * of loss of sensation (neuropathy) and compromised blood flow to and
     * within the lower extremities. You are at increased risk of foot ulcers if
     * you have diabetes or another condition that affects blood flow to the
     * feet, such as peripheral arterial disease (PAD) or phlebitis (inflamed
     * veins). Foot ulcers, which are the focus of this article, are not the
     * same as pressure sores--also known as bed sores or decubitus ulcers--that
     * occur when a person is immobile for an extended period in bed or in a
     * wheelchair. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/foot-ulcers/#
     * sthash.ujE8uWLs.dpuf
     */
    FOOT_ULCERS,

    /**
     * Hallux rigidus is a condition in which the big toe becomes stiff,
     * inflexible and painful, often because of degenerative arthritis at the
     * toe joint—specifically, at the first metatarsophalangeal joint (MPJ),
     * where the first metatarsal bone and proximal phalange of the big toe
     * meet. Bone spurs may also form on the dorsal (top) side of the joint.
     * 
     * The condition is closely related to hallux limitus, in which there is
     * stiffness and pain in the MPJ, but still some limited ability to move the
     * big toe. Hallux limitus is often an early manifestation of hallux
     * rigidus. - See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/hallux-rigidus/#
     * sthash.0tAnHTNz.dpuf
     */
    HALLUX_RIGIDUS,
    
	HALLUX_VALGUS_SEVERE,

    /**
     * Hammertoes are caused by abnormal tension in the muscles and tendons
     * around the toe joints, causing them to buckle or flex. Eventually the
     * joint becomes rigid.
     */
    HAMMERTOES,

    /**
     * A condition such as plantar fasciitis may be caused by poor foot
     * mechanics, such as an overly flattened or overly arched foot. In either
     * case, the fascia—a ligament running along the bottom of your foot—may
     * become irritated and painful.
     * 
     * Painful heel spurs are small bone growths that appear when the ligament
     * running along the sole of your foot tugs repeatedly on the heel bone.
     */
    HEEL_PAIN,
    
    /**
     * A nerve called the posterior tibialis nerve may get trapped, causing
     * irritation and painful symptoms.
     */
    TARSAL_TUNNEL_SYNDROME,

    /**
     * In this condition, thickened tissue wraps around the nerves that lead to
     * your toes. It is usually due to overly tight shoes, arthritis, injury, or
     * malformed bones. The result is nerve compression.
     */
    MORTONS_NEUROMA,

    /**
     * Blisters occur on the feet in areas where the outer layer of skin
     * (epidermis) is subjected to repeated contact friction or shear forces.
     * Those areas can differ based on the type of activity you’re doing, and
     * other factors, including:
     * 
     * Amount of shear External loads (such as a backpack) Presence of moisture
     * Skin characteristics Foot type (high arch, low arch, or medium arch) Type
     * of footwear Fit of your shoes or boots
     * 
     * See more at:
     * http://www.ipfh.org/foot-conditions/foot-conditions-a-z/toe-blisters/#
     * sthash.Kvk7GpWX.dpuf
     */
    TOE_BLISTERS,

    /**
     * Ingrown toenails (usually on the big toe) are caused by inherited
     * abnormalities, incorrect trimming of nails, injury to the toe, infection,
     * or friction from poorly fitted shoes.
     * 
     * Abnormally thick, cracked, and yellowing toenails may be caused by fungal
     * infections, friction from shoes, injuries or conditions such as diabetes
     * or psoriasis.
     */
    TOENAIL,

    /** Some other. */
    OTHER,

}
