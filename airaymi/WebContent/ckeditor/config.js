/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	/*
	config.toolbar = 'Full';
	
	config.toolbar_Full =
	[
	    ['Source','-','Save','NewPage','Preview','-','Templates'],
	    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
	    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	    ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
	    ['BidiLtr', 'BidiRtl'],
	    '/',
	    ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
	    ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
	    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	    ['Link','Unlink','Anchor'],
	    ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
	    '/',
	    ['Styles','Format','Font','FontSize'],
	    ['TextColor','BGColor'],
	    ['Maximize', 'ShowBlocks','-','About']
	];
	*/
	
	/*
	config.toolbar = 'MyToolBar';

	config.toolbar_MyToolBar =
	[
	    ['Save','NewPage','Preview','-','Templates'],
	    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
	    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	    ['BidiLtr', 'BidiRtl'],
	    '/',
	    ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
	    ['NumberedList','BulletedList','-','Outdent','Indent'],
	    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	    ['Link','Unlink','Anchor'],
	    ['Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
	    '/',
	    ['Styles','Format','Font','FontSize'],
	    ['TextColor','BGColor'],
	    ['Maximize', 'ShowBlocks','-','About']
	];
	*/
	
	//config.docType = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">';
	
	//config.fontSize_defaultLabel = '14px';
	
	config.language = "ko";
	
	config.skin = "v2";
	
	config.enterMode = CKEDITOR.ENTER_BR;
	/*
	ENTER_P - <p> paragraphs are created
	ENTER_BR - <br> elements
	ENTER_DIV - <div> blocks are created
	*/
	config.shiftEnterMode = CKEDITOR.ENTER_P;
	/*
	ENTER_P - <p> paragraphs are created
	ENTER_BR - <br> elements
	ENTER_DIV - <div> blocks are created
	*/
	
	config.resize_enabled = false;
	
	config.font_names = '굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Tahoma/Tahoma;Verdana/Verdana';
	
	//the next line add the new font to the combobox in CKEditor 
	
	config.contentsCss = 'fonts.css';
	
	config.font_names = '맑은고딕/Malgun Gothic;' + config.font_names; 
	
	//the next line add the new font to the combobox in CKEditor 
	
	config.font_defaultLabel = '맑은고딕';
	
	//config.extraPlugins = "autogrow";
	
	//config.autoGrow_maxHeight = 800;
	
	//config.extraPlugins = 'devtools';
	
	//config.removePlugins = "resize";

	//config.extraPlugins = "uicolor";
	//config.uiColor = '#14B8C4';
	
	//config.fontSize_sizes = '8/8px;9/9px;10/10px;11/11px;12/12px;14/14px;16/16px;18/18px;20/20px;22/22px;24/24px;26/26px;28/28px;36/36px;48/48px;';
	
	//config.startupFocus = true;
	
	//config.toolbarCanCollapse = false;
	
	//config.menu_subMenuDelay = 0;
	
	config.toolbar = 
	[
	    ['NewPage','Preview','-','Templates'],
	    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
	    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	    ['BidiLtr', 'BidiRtl'],
	    '/',
	    ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
	    ['NumberedList','BulletedList','-','Outdent','Indent'],
	    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	    ['Link','Unlink','Anchor'],
	    ['Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
	    '/',
	    ['Styles','Format','Font','FontSize'],
	    ['TextColor','BGColor'],
	    ['Maximize', 'ShowBlocks','-','About']
	];

	
};
