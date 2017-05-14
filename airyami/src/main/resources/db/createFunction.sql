DELIMITER $$
CREATE DEFINER=`air`@`%` FUNCTION `hierarchy_connect_by_parent_eq_prior_id`(value INT) RETURNS int(11)
    READS SQL DATA
BEGIN
  DECLARE _id INT;
  DECLARE _parent INT;
  DECLARE _next INT;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET @id = NULL;
   
  SET _parent = @id;
  SET _id = -1;
   
  IF @id IS NULL THEN
    RETURN NULL;
  END IF;
   
  LOOP
    SELECT  MIN(MENU_CODE)
      INTO  @id
      FROM  TB_MENU
     WHERE  UPPER_MENU_CODE = _parent
       AND  MENU_CODE > _id;
    
    IF @id IS NOT NULL OR _parent = @start_with THEN
      SET @level = @level + 1;
      RETURN @id;
    END IF;
    
    SET @level := @level - 1;
    SELECT  MENU_CODE, UPPER_MENU_CODE
      INTO  _id, _parent
      FROM  TB_MENU
     WHERE  MENU_CODE = _parent;
  END LOOP;      
END$$
DELIMITER ;
