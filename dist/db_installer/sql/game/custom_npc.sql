--
-- Table structure for table `npc`
-- 
DROP TABLE IF EXISTS `custom_npc`;
CREATE TABLE `custom_npc`(
  `id` decimal(11,0) NOT NULL default '0',
  `displayId` int(11) NOT NULL default '0',
  `name` varchar(200) default NULL,
  `serverSideName` int(1) default '0',
  `title` varchar(45) default '',
  `serverSideTitle` int(1) default '0',
  `class` varchar(200) default NULL,
  `collision_radius` decimal(5,2) default NULL,
  `collision_height` decimal(5,2) default NULL,
  `level` decimal(2,0) default NULL,
  `sex` varchar(6) default NULL,
  `type` varchar(20) default NULL,
  `attackrange` int(11) default NULL,
  `hp` decimal(8,0) default NULL,
  `mp` decimal(5,0) default NULL,
  `hpreg` decimal(8,2) default NULL,
  `mpreg` decimal(5,2) default NULL,
  `str` decimal(7,0) default NULL,
  `con` decimal(7,0) default NULL,
  `dex` decimal(7,0) default NULL,
  `int` decimal(7,0) default NULL,
  `wit` decimal(7,0) default NULL,
  `men` decimal(7,0) default NULL,
  `exp` decimal(9,0) default NULL,
  `sp` decimal(8,0) default NULL,
  `patk` decimal(5,0) default NULL,
  `pdef` decimal(5,0) default NULL,
  `matk` decimal(5,0) default NULL,
  `mdef` decimal(5,0) default NULL,
  `atkspd` decimal(3,0) default NULL,
  `aggro` decimal(6,0) default NULL,
  `matkspd` decimal(4,0) default NULL,
  `rhand` decimal(4,0) default NULL,
  `lhand` decimal(4,0) default NULL,
  `armor` decimal(1,0) default NULL,
  `walkspd` decimal(3,0) default NULL,
  `runspd` decimal(3,0) default NULL,
  `faction_id` varchar(40) default NULL,
  `faction_range` decimal(4,0) default NULL,
  `isUndead` int(11) default 0,
  `absorb_level` decimal(2,0) default 0,
  `absorb_type` enum('FULL_PARTY','LAST_HIT','PARTY_ONE_RANDOM') DEFAULT 'LAST_HIT' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT ignore INTO custom_npc values
('31288', '31228', 'Roy the Cat', '1', 'Class Master', '1', 'Monster.cat_the_cat', '9.00', '16.00', '70', 'male', 'ClassMaster', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '490', '10', '1335', '470', '780', '382', '278', '0', '333', '0', '0', '0', '88', '132', null, '0', '0', '0', 'LAST_HIT'),
('50000', '31228', 'Dom the Cat', '1', 'Merchant', '1', 'Monster.cat_the_cat', '9.00', '16.00', '70', 'male', 'Merchant', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '490', '10', '1335', '470', '780', '382', '278', '0', '333', '0', '0', '0', '88', '132', null, '0', '0', '0', 'LAST_HIT'),
('50007', '31324', 'Andromeda', '1', 'Wedding Manager', '1', 'NPC.a_casino_FDarkElf', '8.00', '23.00', '70', 'female', 'WeddingManager', '40', '3862', '1493', '500', '500', '40', '43', '30', '21', '20', '10', '0', '0', '9999', '9999', '999', '999', '278', '0', '333', '316', '0', '0', '55', '132', null, '0', '1', '0', 'LAST_HIT'),
('50008', '31228', 'Rex the Cat', '1', 'Buffer', '1', 'Monster.cat_the_cat', '9.00', '16.00', '70', 'male', 'SchemeBuffer', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '490', '10', '1335', '470', '780', '382', '278', '0', '333', '0', '0', '0', '88', '132', null, '0', '0', '0', 'LAST_HIT'),
('55555', '22124', 'Totor', '1', 'Rebirth Manager', '1', 'NPC.a_fighterguild_master_FHuman', '11.00', '27.00', '83', 'male', 'Merchant', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '88', '132', null, '0', '0', '0', 'LAST_HIT'),
('70010', '31606', 'Catrina', '1', 'TvT Event Manager', '1', 'Monster2.queen_of_cat', '8.00', '15.00', '70', 'female', 'Npc', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '28', '132', null, '0', '0', '0', 'LAST_HIT'),
('70011', '31606', 'Catretta', '1', 'CTF Event Manager', '1', 'Monster2.queen_of_cat', '8.00', '15.00', '70', 'female', 'Npc', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '28', '132', null, '0', '0', '0', 'LAST_HIT'),
('70012', '31606', 'Catrosa', '1', 'VIP Join Manager', '1', 'Monster2.queen_of_cat', '8.00', '15.00', '70', 'female', 'Npc', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '28', '132', null, '0', '0', '0', 'LAST_HIT'),
('70013', '31606', 'Catrigna', '1', 'VIP End Manager', '1', 'Monster2.queen_of_cat', '8.00', '15.00', '70', 'female', 'Npc', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '28', '132', null, '0', '0', '0', 'LAST_HIT'),
('70014', '31606', 'Catrieta', '1', 'DM Event Manager', '1', 'Monster2.queen_of_cat', '8.00', '15.00', '70', 'female', 'Npc', '40', '3862', '1493', '11.85', '2.78', '40', '43', '30', '21', '20', '10', '0', '0', '1314', '470', '780', '382', '278', '0', '333', '0', '0', '0', '28', '132', null, '0', '0', '0', 'LAST_HIT');
