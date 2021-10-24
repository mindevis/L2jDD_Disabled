/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.model.skills;

/**
 * Abnormal Visual Effect enumerated.
 * @author NosBit
 */
public enum AbnormalVisualEffect
{
	DOT_BLEEDING(1),
	DOT_POISON(2),
	DOT_FIRE(3),
	DOT_WATER(4),
	DOT_WIND(5),
	DOT_SOIL(6),
	STUN(7),
	SLEEP(8),
	SILENCE(9),
	ROOT(10),
	PARALYZE(11),
	FLESH_STONE(12),
	DOT_MP(13),
	BIG_HEAD(14),
	DOT_FIRE_AREA(15),
	CHANGE_TEXTURE(16),
	BIG_BODY(17),
	FLOATING_ROOT(18),
	DANCE_ROOT(19),
	GHOST_STUN(20),
	STEALTH(21),
	SEIZURE1(22),
	SEIZURE2(23),
	MAGIC_SQUARE(24),
	FREEZING(25),
	SHAKE(26),
	ULTIMATE_DEFENCE(28),
	VP_UP(29),
	VP_KEEP(29), // TODO: Unknown ClientID
	REAL_TARGET(30),
	DEATH_MARK(31),
	TURN_FLEE(32),
	INVINCIBILITY(33),
	AIR_BATTLE_SLOW(34),
	AIR_BATTLE_ROOT(35),
	CHANGE_WP(36),
	CHANGE_HAIR_G(37), // Gold Afro
	CHANGE_HAIR_P(38), // Pink Afro
	CHANGE_HAIR_B(39), // Black Afro
	UNKNOWN_40(40),
	STIGMA_OF_SILEN(41),
	SPEED_DOWN(42),
	FROZEN_PILLAR(43),
	CHANGE_VES_S(44),
	CHANGE_VES_C(45),
	CHANGE_VES_D(46),
	TIME_BOMB(47),
	MP_SHIELD(48),
	AIRBIND(49),
	CHANGEBODY(50),
	KNOCKDOWN(51),
	NAVIT_ADVENT(52),
	KNOCKBACK(53),
	CHANGE_7ANNIVERSARY(54),
	ON_SPOT_MOVEMENT(55),
	DEPORT(56),
	AURA_BUFF(57),
	AURA_BUFF_SELF(58),
	AURA_DEBUFF(59),
	AURA_DEBUFF_SELF(60),
	HURRICANE(61),
	HURRICANE_SELF(62),
	BLACK_MARK(63),
	BR_SOUL_AVATAR(64),
	CHANGE_GRADE_B(65),
	BR_BEAM_SWORD_ONEHAND(66),
	BR_BEAM_SWORD_DUAL(67),
	NO_CHAT(68),
	HERB_PA_UP(69),
	HERB_MA_UP(70),
	SEED_TALISMAN1(71),
	SEED_TALISMAN2(72),
	SEED_TALISMAN3(73),
	SEED_TALISMAN4(74),
	SEED_TALISMAN5(75),
	SEED_TALISMAN6(76),
	CURIOUS_HOUSE(77),
	NGRADE_CHANGE(78),
	DGRADE_CHANGE(79),
	CGRADE_CHANGE(80),
	BGRADE_CHANGE(81),
	AGRADE_CHANGE(82),
	SWEET_ICE_FLAKES(83),
	FANTASY_ICE_FLAKES(84),
	CHANGE_XMAS(85),
	CARD_PC_DECO(86),
	CHANGE_DINOS(87),
	CHANGE_VALENTINE(88),
	CHOCOLATE(89),
	CANDY(90),
	COOKIE(91),
	STARS_0(92),
	STARS_1(93),
	STARS_2(94),
	STARS_3(95),
	STARS_4(96),
	STARS_5(97),
	DUELING(98),
	FREEZING2(99),
	CHANGE_YOGI(100),
	YOGI(101),
	MUSICAL_NOTE_YELLOW(102),
	MUSICAL_NOTE_BLUE(103),
	MUSICAL_NOTE_GREEN(104),
	TENTH_ANNIVERSARY(105),
	XMAS_SOCKS(106),
	XMAS_TREE(107),
	XMAS_SNOWMAN(108),
	OTHELL_ROGUE_BLUFF(109),
	HE_PROTECT(110),
	SU_SUMCROSS(111),
	WIND_STUN(112),
	STORM_SIGN2(113),
	STORM_SIGN1(114),
	WIND_BLEND(115),
	DECEPTIVE_BLINK(116),
	WIND_HIDE(117),
	PSY_POWER(118),
	SQUALL(119),
	WIND_ILLUSION(120),
	SAYHA_FURY(121),
	HIDE4(123),
	PMENTAL_TRAIL(124),
	HOLD_LIGHTING(125),
	GRAVITY_SPACE_3(126),
	SPACEREF(127),
	HE_ASPECT(128),
	RUNWAY_ARMOR1(129),
	RUNWAY_ARMOR2(130),
	RUNWAY_ARMOR3(131),
	RUNWAY_ARMOR4(132),
	RUNWAY_ARMOR5(133),
	RUNWAY_ARMOR6(134),
	RUNWAY_WEAPON1(135),
	RUNWAY_WEAPON2(136),
	PALADIN_PROTECTION(141),
	SENTINEL_PROTECTION(142),
	REAL_TARGET_2(143),
	DIVINITY(144),
	SHILLIEN_PROTECTION(145),
	EVENT_STARS_0(146),
	EVENT_STARS_1(147),
	EVENT_STARS_2(148),
	EVENT_STARS_3(149),
	EVENT_STARS_4(150),
	EVENT_STARS_5(151),
	ABSORB_SHIELD(152),
	PHOENIX_AURA(153),
	REVENGE_AURA(154),
	EVAS_AURA(155),
	TEMPLAR_AURA(156),
	LONG_BLOW(157),
	WIDE_SWORD(158),
	BIG_FIST(159),
	SHADOW_STEP(160),
	TORNADO(161),
	SNOW_SLOW(162),
	SNOW_HOLD(163),
	UNK_164(164),
	TORNADO_SLOW(165),
	ASTATINE_WATER(166),
	BIG_BODY_COMBINATION_CAT_NPC(167),
	BIG_BODY_COMBINATION_UNICORN_NPC(168),
	BIG_BODY_COMBINATION_DEMON_NPC(169),
	BIG_BODY_COMBINATION_CAT_PC(170),
	BIG_BODY_COMBINATION_UNICORN_PC(171),
	BIG_BODY_COMBINATION_DEMON_PC(172),
	BIG_BODY_2(173),
	BIG_BODY_3(174),
	PIRATE_SUIT(175),
	DARK_ASSASSIN_SUIT(176),
	WHITE_ASSASSIN_SUIT(177),
	UNK_178(178),
	UNK_179(179),
	UNK_180(180),
	AVE_DRAGON_ULTIMATE(181),
	HALLOWEEN_SUIT(182),
	INFINITE_SHIELD1_AVE(183),
	INFINITE_SHIELD2_AVE(184),
	INFINITE_SHIELD3_AVE(185),
	INFINITE_SHIELD4_AVE(186),
	AVE_ABSORB2_SHIELD(187),
	UNK_188(188),
	UNK_189(189),
	TALISMAN_BAIUM(190),
	UNK_191(191),
	UNK_192(192),
	CHANGESHAPE_TRANSFORM(193),
	ANGRY_GOLEM_AVE(194),
	WA_UNBREAKABLE_SONIC_AVE(195),
	HEROIC_HOLY_AVE(196),
	HEROIC_SILENCE_AVE(197),
	HEROIC_FEAR_AVE_1(198),
	HEROIC_FEAR_AVE_2(199),
	AVE_BROOCH(200),
	AVE_BROOCH_B(201),
	INFINITE_SHIELD4_AVE_2(206),
	CHANGESHAPE_TRANSFORM_1(207),
	CHANGESHAPE_TRANSFORM_2(208),
	CHANGESHAPE_TRANSFORM_3(209),
	CHANGESHAPE_TRANSFORM_4(210),
	RO_COUNTER_TRASPIE(215),
	RO_GHOST_REFLECT(217),
	CHANGESHAPE_TRANSFORM_5(218),
	ICE_ELEMENTALDESTROY(219),
	DORMANT_USER(220),
	NUWBIE_USER(221),
	THIRTEENTH_BUFF(222),
	ARENA_UNSEAL_A(224),
	ARENA_UNSEAL_B(225),
	ARENA_UNSEAL_C(226),
	ARENA_UNSEAL_D(227),
	ARENA_UNSEAL_E(228),
	IN_BATTLE_RHAPSODY(229),
	IN_A_DECAL(230),
	IN_B_DECAL(231),
	CHANGESHAPE_TRANSFORM_6(232),
	CHANGESHAPE_TRANSFORM_7(234),
	SPIRIT_KING_WIND_AVE_B(235),
	EARTH_KING_BARRIER1_AVE(236),
	EARTH_KING_BARRIER2_AVE(237),
	FOCUS_SHIELD(247),
	RAISE_SHIELD(248),
	TRUE_VANGUARD(249),
	SHIELD_WALL(250),
	FOOT_TRAIL(251),
	LEGEND_DECO_HERO(252),
	CHANGESHAPE_TRANSFORM_8(253),
	SPIRIT_KING_WIND_AVE(254),
	U098_BUFF_TA_DECO(255),
	U098_RIGHT_DECO(255),
	U098_LEFT_DECO(255),
	ORFEN_ENERGY1_AVE(256),
	ORFEN_ENERGY2_AVE(257),
	ORFEN_ENERGY3_AVE(258),
	ORFEN_ENERGY4_AVE(259),
	ORFEN_ENERGY5_AVE(260),
	UNHOLY_BARRIER_AVE(263),
	RUDOLF_A_AVE(266),
	RUDOLF_B_AVE(267),
	RUDOLF_C_AVE(268),
	OLYMPIAD_MEDAL_AVE(269),
	OLYMPIAD_SPORT_A_AVE(270),
	OLYMPIAD_SPORT_B_AVE(271),
	OLYMPIAD_SPORT_C_AVE(272),
	D_CHAOS_MATCH_DECO_SMALL(273),
	TRANS_DECO_R(274),
	TRANS_DECO_B(275),
	TRANS_DECO_Y(276),
	TRANS_DECO_G(277),
	TRANS_DECO_P(278),
	TRANS_DECO_P2(279),
	TRANS_DECO_W(280),
	TRANS_DECO_R2(281),
	TRANS_DECO_W2(282),
	HDDOWN_AVE(283),
	HDMDOWN_AVE(284),
	KISSNHEART_AVE(286),
	EARTH_BARRIER_AVE(287),
	LILITH_DARK_BARRIER_AVE(288),
	EARTH_BARRIER2_AVE(289),
	CROFFIN_QUEEN_INVINCIBILITY_AVE(290),
	MPDOWN_AVE(291),
	WORLDCUP_RED_AVE(292),
	WORLDCUP_BLUE_AVE(293),
	SURGEWAVE_AVE(296),
	BLESS_AVE(297),
	ANTHARAS_RAGE_AVE(298),
	G_BARRIER_AVE(302),
	FIREWORKS_001T(304),
	FIREWORKS_002T(305),
	FIREWORKS_003T(306),
	FIREWORKS_004T(307),
	FIREWORKS_005T(308),
	FIREWORKS_006T(309),
	FIREWORKS_007T(310),
	FIREWORKS_008T(311),
	FIREWORKS_009T(312),
	FIREWORKS_010T(313),
	FIREWORKS_011T(314),
	FIREWORKS_012T(315),
	FIREWORKS_013T(316),
	FIREWORKS_014T(317),
	FIREWORKS_015T(318),
	P_CAKE_AVE(319),
	ZARICHE_PRISION_AVE(323),
	RUDOLPH(324),
	XMAS_HEART_AVE(325),
	XMAS_HAND_AVE(326),
	LUCKYBAG_AVE(327),
	HEROIC_MIRACLE_AVE(328),
	POCKETPIG_AVE(329),
	BLACK_STANCE_AVE(330),
	BLACK_TRANS_DECO_AVE(330),
	KAMAEL_BLACK_TRANSFORM(331),
	WHITE_STANCE_AVE(332),
	HEAL_DECO_AVE(332),
	KAMAEL_WHITE_TRANSFORM(333),
	LONG_RAPIER_WHITE_AVE(334),
	LONG_RAPIER_BLACK_AVE(335),
	ZARICHE_PRISION2_AVE(336),
	BLUEHEART_AVE(337),
	ATTACK_BUFF_AVE(338),
	SHIELD_BUFF_AVE(339),
	BERSERKER_BUFF_AVE(340),
	SEED_TALISMAN8(341),
	H_GOLD_STAR1_AVE(342),
	H_GOLD_STAR2_AVE(343),
	H_GOLD_STAR3_AVE(344),
	H_GOLD_STAR4_AVE(345),
	H_GOLD_STAR5_AVE(346),
	AVE_RAID_AREA(347),
	H_HEART_ADEN_AVE(348),
	H_ADENBAG_COIN_AVE(349),
	KAMAEL_BLACK_TRANSFORM_2(350),
	KAMAEL_WHITE_TRANSFORM_2(351),
	H_DEATH_EFFECT_AVE(352),
	AVE_WHITE_KNIGHT(353),
	U_ER_WI_WINDHIDE_AVE(354),
	RANKER_DECO_HUMAN(385),
	RANKER_DECO_KAMAEL(386),
	JDK_BODY_FIRE_1(387),
	DK_BONEPRISON_AVE(388),
	DK_CHANGE_ARMOR(389),
	DK_IGNITION_HUMAN_AVE(390),
	DK_IGNITION_ELF_AVE(391),
	DK_IGNITION_DARKELF_AVE(392),
	DK_ACCELERATION_AVE(393),
	DK_BURN_AVE(394),
	DK_FREEZING_AREA_AVE(395),
	DK_SHOCK_AVE(396),
	DK_PERFECT_SHILED_AVE(397),
	DK_FROSTBITE_AVE(398),
	DK_BONEPRISON_SQUELA_AVE(399),
	H_B_HASTE_B_AVE(400),
	FS_STIGMA_AVE(401),
	FORT_FLAG_AVE(403),
	H_EVENT_MOON_AVE(404),
	H_R_BARRIER_AVE(409),
	H_R_FIRE_DEBUFF_AVE(410),
	H_P_FIRE_DEBUFF_AVE(411),
	H_EVENT_PUMPKIN_AVE(418),
	H_EVENT_PUMPKIN_B_AVE(419),
	AVE_POISON_GROUND_G(420),
	AVE_POISON_GROUND_B(421),
	AVE_POISON_GROUND_P(422),
	AVE_POISON_GROUND_R(423),
	RANKER_DECO_ORC(424),
	H_DEBUFF_SELF_B_AVE(425),
	H_AURA_DEBUFF_B_AVE(426),
	H_ULTIMATE_DEFENCE_B_AVE(427),
	RANKER_DECO_ELF(428),
	RANKER_DECO_DARKELF(429),
	RANKER_DECO_DWARF(430),
	H_Y_MAGNETIC_AVE(431),
	H_R_NATURAL_BEAST_AVE(433),
	H_BERSERKER_B_BUFF_AVE(435),
	H_BERSERKER_C_BUFF_AVE(436),
	U_AVE_DIVINITY(437),
	Y_RO_GHOST_REFLECT_AVE(438),
	S_EVENT_KITE_DECO(439),
	H_B_SYMPHONY_SWORD_AVE(441),
	H_B_SYMPHONY_SWORD_DEFENCE_AVE(442),
	H_B_SYMPHONY_SWORD_BUFF_A_AVE(443),
	H_B_SYMPHONY_SWORD_BUFF_B_AVE(444),
	H_G_POISON_DANCE_AVE(445),
	H_G_POISON_DANCE_DEBUFF_A_AVE(446),
	H_G_POISON_DANCE_DEBUFF_B_AVE(447),
	H_R_POISON_DANCE_BUFF_B_AVE(448),
	H_B_CHOCOLATE_AVE(450),
	H_P_CHAIN_BLOCK_AVE(451),
	H_EVENT_MASK_AVE(452),
	H_R_ORC_TITAN_AVE(454),
	H_R_GIGANTIC_WEAPON_AVE(455),
	H_B_TOTEM_PUMA_AVE(457),
	H_Y_TOTEM_RABBIT_AVE(458),
	H_G_TOTEM_OGRE_AVE(459),
	H_Y_ORC_HP_AVE(460),
	H_B_ORC_HP_AVE(461),
	V_ORC_IMMOLATION_BODY_AVE(462),
	H_R_ORC_WAR_ROAR_AVE(463),
	DRAGON_ULTIMATE(700),
	CHANGE_HALLOWEEN(1000),
	BR_Y_1_ACCESSORY_R_RING(10001),
	BR_Y_1_ACCESSORY_EARRING(10002),
	BR_Y_1_ACCESSORY_NECKRACE(10003),
	BR_Y_2_ACCESSORY_R_RING(10004),
	BR_Y_2_ACCESSORY_EARRING(10005),
	BR_Y_2_ACCESSORY_NECKRACE(10006),
	BR_Y_3_ACCESSORY_R_RING(10007),
	BR_Y_3_ACCESSORY_EARRING(10008),
	BR_Y_3_ACCESSORY_NECKRACE(10009),
	BR_Y_3_TALI_DECO_WING(10019),
	S_TRANS_DECO_1(10021),
	BR_TRANS_LV2_DECO_1(10022),
	BR_TRANS_LV3_DECO_1(10023),
	S_TRANS_DECO_2(10024),
	BR_TRANS_LV2_DECO_2(10025),
	BR_TRANS_LV3_DECO_2(10026),
	S_TRANS_DECO_3(10027),
	BR_TRANS_LV2_DECO_3(10028),
	BR_TRANS_LV3_DECO_3(10029),
	S_TRANS_DECO_4(10030),
	BR_TRANS_LV2_DECO_4(10031),
	BR_TRANS_LV3_DECO_4(10032),
	S_TRANS_DECO_5(10033),
	BR_TRANS_LV2_DECO_5(10034),
	BR_TRANS_LV3_DECO_5(10035),
	S_TRANS_DECO_6(10036),
	BR_TRANS_LV2_DECO_6(10037),
	BR_TRANS_LV3_DECO_6(10038),
	S_TRANS_DECO_7(10039),
	BR_TRANS_LV2_DECO_7(10040),
	BR_TRANS_LV3_DECO_7(10041),
	S_TRANS_DECO_8(10042),
	BR_TRANS_LV2_DECO_8(10043),
	BR_TRANS_LV3_DECO_8(10044),
	S_TRANS_DECO_9(10045),
	BR_TRANS_LV2_DECO_9(10046),
	BR_TRANS_LV3_DECO_9(10047),
	S_TRANS_DECO_10(10048),
	BR_TRANS_LV2_DECO_10(10049),
	BR_TRANS_LV3_DECO_10(10050),
	S_TRANS_DECO_11(10051),
	BR_TRANS_LV2_DECO_11(10052),
	BR_TRANS_LV3_DECO_11(10053),
	S_TRANS_DECO_12(10054),
	BR_TRANS_LV2_DECO_12(10055),
	BR_TRANS_LV3_DECO_12(10056),
	S_TRANS_DECO_13(10057),
	BR_TRANS_LV2_DECO_13(10058),
	BR_TRANS_LV3_DECO_13(10059),
	S_TRANS_DECO_14(10060),
	BR_TRANS_LV2_DECO_14(10061),
	BR_TRANS_LV3_DECO_14(10062),
	S_TRANS_DECO_15(10063),
	BR_TRANS_LV2_DECO_15(10064),
	BR_TRANS_LV3_DECO_15(10065),
	S_TRANS_DECO_16(10066),
	BR_TRANS_LV2_DECO_16(10067),
	BR_TRANS_LV3_DECO_16(10068),
	S_TRANS_DECO_17(10069),
	BR_TRANS_LV2_DECO_17(10070),
	BR_TRANS_LV3_DECO_17(10071),
	S_TRANS_DECO_18(10072),
	BR_TRANS_LV2_DECO_18(10073),
	BR_TRANS_LV3_DECO_18(10074),
	S_TRANS_DECO_19(10075),
	BR_TRANS_LV2_DECO_19(10076),
	BR_TRANS_LV3_DECO_19(10077),
	S_TRANS_DECO_20(10078),
	BR_TRANS_LV2_DECO_20(10079),
	BR_TRANS_LV3_DECO_20(10080),
	S_TRANS_DECO_21(10081),
	BR_TRANS_LV2_DECO_21(10082),
	BR_TRANS_LV3_DECO_21(10083),
	S_TRANS_DECO_22(10084),
	BR_TRANS_LV2_DECO_22(10085),
	BR_TRANS_LV3_DECO_22(10086),
	S_TRANS_DECO_23(10087),
	BR_TRANS_LV2_DECO_23(10088),
	BR_TRANS_LV3_DECO_23(10089),
	S_TRANS_DECO_24(10090),
	BR_TRANS_LV2_DECO_24(10091),
	BR_TRANS_LV3_DECO_24(10092),
	S_TRANS_DECO_25(10093),
	BR_TRANS_LV2_DECO_25(10094),
	BR_TRANS_LV3_DECO_25(10095),
	S_TRANS_DECO_26(10096),
	BR_TRANS_LV2_DECO_26(10097),
	BR_TRANS_LV3_DECO_26(10098),
	S_TRANS_DECO_27(10099),
	BR_TRANS_LV2_DECO_27(10100),
	BR_TRANS_LV3_DECO_27(10101),
	S_TRANS_DECO_28(10102),
	BR_TRANS_LV2_DECO_28(10103),
	BR_TRANS_LV3_DECO_28(10104),
	S_TRANS_DECO_29(10105),
	BR_TRANS_LV2_DECO_29(10106),
	BR_TRANS_LV3_DECO_29(10107),
	S_TRANS_DECO_30(10108),
	BR_TRANS_LV2_DECO_30(10109),
	BR_TRANS_LV3_DECO_30(10110),
	S_TRANS_DECO_31(10111),
	BR_TRANS_LV2_DECO_31(10112),
	BR_TRANS_LV3_DECO_31(10113),
	S_TRANS_DECO_32(10114),
	BR_TRANS_LV2_DECO_32(10115),
	BR_TRANS_LV3_DECO_32(10116),
	S_TRANS_DECO_33(10117),
	BR_TRANS_LV2_DECO_33(10118),
	BR_TRANS_LV3_DECO_33(10119),
	S_TRANS_DECO_34(10120),
	BR_TRANS_LV2_DECO_34(10121),
	BR_TRANS_LV3_DECO_34(10122),
	S_TRANS_DECO_35(10123),
	BR_TRANS_LV2_DECO_35(10124),
	BR_TRANS_LV3_DECO_35(10125),
	S_TRANS_DECO_36(10126),
	BR_TRANS_LV2_DECO_36(10127),
	BR_TRANS_LV3_DECO_36(10128),
	S_TRANS_DECO_37(10129),
	BR_TRANS_LV2_DECO_37(10130),
	BR_TRANS_LV3_DECO_37(10131),
	S_TRANS_DECO_38(10132),
	BR_TRANS_LV2_DECO_38(10133),
	BR_TRANS_LV3_DECO_38(10134),
	S_TRANS_DECO_39(10135),
	BR_TRANS_LV2_DECO_39(10136),
	BR_TRANS_LV3_DECO_39(10137),
	S_TRANS_DECO_40(10138),
	BR_TRANS_LV2_DECO_40(10139),
	BR_TRANS_LV3_DECO_40(10140),
	S_TRANS_DECO_41(10141),
	BR_TRANS_LV2_DECO_41(10142),
	BR_TRANS_LV3_DECO_41(10143),
	S_TRANS_DECO_42(10144),
	BR_TRANS_LV2_DECO_42(10145),
	BR_TRANS_LV3_DECO_42(10146),
	S_TRANS_DECO_43(10147),
	BR_TRANS_LV2_DECO_43(10148),
	BR_TRANS_LV3_DECO_43(10149),
	S_TRANS_DECO_44(10150),
	BR_TRANS_LV2_DECO_44(10151),
	BR_TRANS_LV3_DECO_44(10152),
	S_TRANS_DECO_45(10153),
	BR_TRANS_LV2_DECO_45(10154),
	BR_TRANS_LV3_DECO_45(10155),
	S_TRANS_DECO_46(10156),
	BR_TRANS_LV2_DECO_46(10157),
	BR_TRANS_LV3_DECO_46(10158),
	S_TRANS_DECO_47(10159),
	BR_TRANS_LV2_DECO_47(10160),
	BR_TRANS_LV3_DECO_47(10161),
	JDK_BODY_FIRE_2(10162);
	
	private final int _clientId;
	
	AbnormalVisualEffect(int clientId)
	{
		_clientId = clientId;
	}
	
	/**
	 * Gets the client id.
	 * @return the client id
	 */
	public int getClientId()
	{
		return _clientId;
	}
	
	/**
	 * Finds abnormal visual effect by name.
	 * @param name the name
	 * @return The abnormal visual effect if its found, {@code null} otherwise
	 */
	public static AbnormalVisualEffect findByName(String name)
	{
		for (AbnormalVisualEffect abnormalVisualEffect : values())
		{
			if (abnormalVisualEffect.name().equalsIgnoreCase(name))
			{
				return abnormalVisualEffect;
			}
		}
		return null;
	}
}