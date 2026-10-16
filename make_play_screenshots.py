#!/usr/bin/env python3
import subprocess
import os

OUTPUT_DIR = "play_store_screenshots"
os.makedirs(OUTPUT_DIR, exist_ok=True)

def run_im(args):
    cmd = ["convert"] + args
    res = subprocess.run(cmd, capture_output=True, text=True)
    if res.returncode != 0:
        print("IM Error:", res.stderr)
        raise RuntimeError(res.stderr)

def make_screenshot_1():
    # 1. Welcome & Brand Splash
    out_file = os.path.join(OUTPUT_DIR, "screenshot_1_welcome.png")
    args = [
        "-size", "1080x1920", "xc:#F7F4EB",
        # Top gradient header bar
        "-fill", "#0F766E", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "64", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "WORD FINDER MAGIC",
        "-font", "DejaVu-Sans", "-pointsize", "34", "-fill", "#CCFBF1",
        "-gravity", "north", "-annotate", "+0+155", "Educational Word Search Puzzles for Kids",

        # Mascot Emblem outer white die-cut circle + shadow
        "-fill", "#E2D9C5", "-draw", "circle 540,780 540,1110",
        "-fill", "#FFFFFF", "-draw", "circle 540,770 540,1090",
        "-fill", "#F5EEDB", "-draw", "circle 540,770 540,1065",

        # Mascot Sunburst Flames
        "-fill", "#DEC180", "-draw", "circle 450,770 450,890",
        "-fill", "#DEC180", "-draw", "circle 630,770 630,890",
        "-fill", "#F5EEDB", "-draw", "circle 540,820 540,940",

        # Top Arched text MBHKAT
        "-font", "DejaVu-Sans-Bold", "-pointsize", "56", "-fill", "#115E59",
        "-gravity", "north", "-annotate", "+0+535", "M B H K A T",

        # Cat Head & Body
        "-fill", "#0F766E", "-stroke", "#115E59", "-strokewidth", "4",
        "-draw", "circle 540,750 540,845", # head
        "-fill", "#0F766E", "-draw", "roundrectangle 470,780 610,950 50,50", # body
        "-draw", "polygon 470,690 490,620 520,680", # left ear
        "-draw", "polygon 610,690 590,620 560,680", # right ear
        "-fill", "#2DD4BF", "-stroke", "none",
        "-draw", "polygon 480,680 492,635 510,675", # left inner ear
        "-draw", "polygon 600,680 588,635 570,675", # right inner ear

        # Smiling Cat Face
        "-stroke", "#FFFFFF", "-strokewidth", "6", "-fill", "none",
        "-draw", "arc 495,725 525,755 200,340", # left smiling eye
        "-draw", "arc 555,725 585,755 200,340", # right smiling eye
        "-fill", "#115E59", "-stroke", "none",
        "-draw", "polygon 535,760 545,760 540,766", # nose
        "-stroke", "#115E59", "-strokewidth", "4", "-fill", "none",
        "-draw", "arc 528,764 540,776 0,180", # mouth left
        "-draw", "arc 540,764 552,776 0,180", # mouth right
        # Whiskers
        "-draw", "line 480,755 435,745",
        "-draw", "line 480,765 430,765",
        "-draw", "line 480,775 435,785",
        "-draw", "line 600,755 645,745",
        "-draw", "line 600,765 650,765",
        "-draw", "line 600,775 645,785",
        # Tail
        "-fill", "#0F766E", "-stroke", "#115E59", "-strokewidth", "4",
        "-draw", "arc 580,840 680,940 270,90",

        # Bottom Arched text KAT
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "62", "-fill", "#115E59",
        "-gravity", "north", "-annotate", "+0+985", "K A T",

        # Feature Badges Card Container
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "3",
        "-draw", "roundrectangle 80,1160 1000,1650 36,36",

        "-stroke", "none",
        # Feature row 1
        "-fill", "#FEF3C7", "-draw", "circle 170,1250 170,1285",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+230+1220", "Pre-K to 5th Grade Curriculum",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#64748B",
        "-gravity", "northwest", "-annotate", "+230+1268", "7 calibrated difficulties & grid sizes",

        # Feature row 2
        "-fill", "#E0F2FE", "-draw", "circle 170,1380 170,1415",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+230+1350", "8 Built-in Themed Dictionaries",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#64748B",
        "-gravity", "northwest", "-annotate", "+230+1398", "Animals, Space, Ocean, Magic, Food & Sight Words",

        # Feature row 3
        "-fill", "#DCFCE7", "-draw", "circle 170,1510 170,1545",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+230+1480", "Printable Worksheets & Audio Phonics",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#64748B",
        "-gravity", "northwest", "-annotate", "+230+1528", "One-tap PDF printing for classrooms & homework",

        # Bottom Call to Action Button
        "-fill", "#0F766E", "-draw", "roundrectangle 140,1720 940,1830 30,30",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "38", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+1750", "START PLAYING FREE ✨",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

def make_screenshot_2():
    # 2. Grade Level Curriculum
    out_file = os.path.join(OUTPUT_DIR, "screenshot_2_grades.png")
    args = [
        "-size", "1080x1920", "xc:#F8FAFC",
        # Top banner
        "-fill", "#4F46E5", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "60", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "7 CURRICULUM GRADES",
        "-font", "DejaVu-Sans", "-pointsize", "32", "-fill", "#E0E7FF",
        "-gravity", "north", "-annotate", "+0+155", "From Pre-K Starter Words to 5th Grade Vocabulary",

        # Grade selector pills row
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "2",
        "-draw", "roundrectangle 60,280 1020,490 28,28",

        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#334155",
        "-gravity", "northwest", "-annotate", "+90+310", "Select Grade Level:",

        # Chips
        "-fill", "#4F46E5", "-draw", "roundrectangle 90,370 240,440 20,20",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "24", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+120+392", "Pre-K",

        "-fill", "#EEF2FF", "-draw", "roundrectangle 260,370 410,440 20,20",
        "-fill", "#4338CA",
        "-gravity", "northwest", "-annotate", "+285+392", "Kinder",

        "-fill", "#EEF2FF", "-draw", "roundrectangle 430,370 560,440 20,20",
        "-gravity", "northwest", "-annotate", "+455+392", "1st Gr",

        "-fill", "#EEF2FF", "-draw", "roundrectangle 580,370 710,440 20,20",
        "-gravity", "northwest", "-annotate", "+605+392", "2nd Gr",

        "-fill", "#EEF2FF", "-draw", "roundrectangle 730,370 860,440 20,20",
        "-gravity", "northwest", "-annotate", "+755+392", "3rd Gr",

        "-fill", "#EEF2FF", "-draw", "roundrectangle 880,370 1010,440 20,20",
        "-gravity", "northwest", "-annotate", "+905+392", "4th Gr",

        # Quick Play Hero Card
        "-fill", "#4F46E5", "-draw", "roundrectangle 60,530 1020,680 30,30",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "40", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+140+580", "▶  Quick Play (Kindergarten 6×6)",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#C7D2FE",
        "-gravity", "northwest", "-annotate", "+140+630", "Jump right into a fresh calibrated puzzle",

        # Themes Grid Section Header
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+60+720", "Choose a Fun Theme:",

        # Theme Card 1: Animal Safari
        "-fill", "#ECFDF5", "-stroke", "#A7F3D0", "-strokewidth", "3",
        "-draw", "roundrectangle 60,780 520,1080 26,26",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#065F46",
        "-gravity", "northwest", "-annotate", "+100+830", "🐾 Animal Safari",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#047857",
        "-gravity", "northwest", "-annotate", "+100+890", "Lion, Tiger, Bear, Duck...",
        "-fill", "#10B981", "-draw", "roundrectangle 100,990 320,1045 16,16",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "22", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+135+1007", "Play 6 Words",

        # Theme Card 2: Cosmic Galaxy
        "-fill", "#EFF6FF", "-stroke", "#BFDBFE", "-strokewidth", "3",
        "-draw", "roundrectangle 560,780 1020,1080 26,26",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#1E40AF",
        "-gravity", "northwest", "-annotate", "+600+830", "🚀 Cosmic Galaxy",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#2563EB",
        "-gravity", "northwest", "-annotate", "+600+890", "Planet, Star, Comet, Moon...",
        "-fill", "#3B82F6", "-draw", "roundrectangle 600,990 820,1045 16,16",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "22", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+635+1007", "Play 6 Words",

        # Theme Card 3: Ocean Splash
        "-fill", "#F0FDFA", "-stroke", "#99F6E4", "-strokewidth", "3",
        "-draw", "roundrectangle 60,1120 520,1420 26,26",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#115E59",
        "-gravity", "northwest", "-annotate", "+100+1170", "🌊 Ocean Splash",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#0F766E",
        "-gravity", "northwest", "-annotate", "+100+1230", "Fish, Whale, Coral, Shark...",
        "-fill", "#0D9488", "-draw", "roundrectangle 100,1330 320,1385 16,16",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "22", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+135+1347", "Play 6 Words",

        # Theme Card 4: Magic Kingdom
        "-fill", "#FAF5FF", "-stroke", "#E9D5FF", "-strokewidth", "3",
        "-draw", "roundrectangle 560,1120 1020,1420 26,26",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#6B21A8",
        "-gravity", "northwest", "-annotate", "+600+1170", "🏰 Magic Kingdom",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#7E22CE",
        "-gravity", "northwest", "-annotate", "+600+1230", "Crown, Wizard, Dragon...",
        "-fill", "#9333EA", "-draw", "roundrectangle 600,1330 820,1385 16,16",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "22", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+635+1347", "Play 6 Words",

        # Custom Theme Creator Promo
        "-fill", "#FFFBEB", "-stroke", "#FDE68A", "-strokewidth", "3",
        "-draw", "roundrectangle 60,1470 1020,1650 26,26",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#92400E",
        "-gravity", "northwest", "-annotate", "+120+1515", "🎨 Custom Theme & Spelling Creator",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#B45309",
        "-gravity", "northwest", "-annotate", "+120+1575", "Add your child's weekly school spelling words in seconds!",

        # Bottom Safe Learning Banner
        "-fill", "#0F766E", "-draw", "roundrectangle 60,1710 1020,1830 26,26",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "32", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+1745", "100% Kid Safe • No Ads • Offline Play",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#CCFBF1",
        "-gravity", "north", "-annotate", "+0+1785", "Dolch & Fry sight word lists included",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

def make_screenshot_3():
    # 3. Interactive Word Search Gameplay
    out_file = os.path.join(OUTPUT_DIR, "screenshot_3_gameplay.png")
    args = [
        "-size", "1080x1920", "xc:#F8FAFC",
        # Top banner
        "-fill", "#0284C7", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "60", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "SMOOTH TOUCH SEARCH",
        "-font", "DejaVu-Sans", "-pointsize", "32", "-fill", "#E0F2FE",
        "-gravity", "north", "-annotate", "+0+155", "Straight-line drag with colorful highlight ribbons",

        # In-game Top Bar
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "2",
        "-draw", "roundrectangle 60,270 1020,380 24,24",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "32", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+100+310", "🐾 Animal Safari",
        "-font", "DejaVu-Sans", "-pointsize", "22", "-fill", "#4F46E5",
        "-gravity", "northwest", "-annotate", "+360+318", "1st Grade • 7×7",

        "-fill", "#FEF3C7", "-draw", "roundrectangle 760,295 980,355 16,16",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "24", "-fill", "#B45309",
        "-gravity", "northwest", "-annotate", "+785+312", "💡 3 Hints  ⏱ 01:24",

        # White Grid Card Container
        "-fill", "#FFFFFF", "-stroke", "#CBD5E1", "-strokewidth", "3",
        "-draw", "roundrectangle 60,420 1020,1380 32,32",

        # Highlight Ribbons on Grid
        # Word 1: L I O N (row 1, col 1 to 4) horizontal blue
        "-fill", "#BAE6FD", "-draw", "roundrectangle 160,530 680,630 45,45",
        # Word 2: B E A R (col 5, row 2 to 5) vertical amber
        "-fill", "#FDE68A", "-draw", "roundrectangle 700,650 800,1190 45,45",
        # Word 3: D U C K (row 5, col 0 to 3) horizontal mint
        "-fill", "#A7F3D0", "-draw", "roundrectangle 160,1070 680,1170 45,45",

        # Letters on 7x7 Grid
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "44", "-fill", "#0F172A",

        # Row 0
        "-gravity", "northwest",
        "-annotate", "+200+460", "F", "-annotate", "+330+460", "K", "-annotate", "+460+460", "P",
        "-annotate", "+590+460", "Z", "-annotate", "+720+460", "T", "-annotate", "+850+460", "X",

        # Row 1: L I O N
        "-annotate", "+200+560", "L", "-annotate", "+330+560", "I", "-annotate", "+460+560", "O",
        "-annotate", "+590+560", "N", "-annotate", "+720+560", "M", "-annotate", "+850+560", "W",

        # Row 2: B in BEAR
        "-annotate", "+200+690", "A", "-annotate", "+330+690", "C", "-annotate", "+460+690", "V",
        "-annotate", "+590+690", "G", "-annotate", "+720+690", "B", "-annotate", "+850+690", "Q",

        # Row 3: E in BEAR
        "-annotate", "+200+820", "R", "-annotate", "+330+820", "S", "-annotate", "+460+820", "Y",
        "-annotate", "+590+820", "H", "-annotate", "+720+820", "E", "-annotate", "+850+820", "J",

        # Row 4: A in BEAR
        "-annotate", "+200+950", "P", "-annotate", "+330+950", "N", "-annotate", "+460+950", "L",
        "-annotate", "+590+950", "B", "-annotate", "+720+950", "A", "-annotate", "+850+950", "K",

        # Row 5: D U C K and R in BEAR
        "-annotate", "+200+1090", "D", "-annotate", "+330+1090", "U", "-annotate", "+460+1090", "C",
        "-annotate", "+590+1090", "K", "-annotate", "+720+1090", "R", "-annotate", "+850+1090", "E",

        # Row 6
        "-annotate", "+200+1230", "Z", "-annotate", "+330+1230", "O", "-annotate", "+460+1230", "O",
        "-annotate", "+590+1230", "M", "-annotate", "+720+1230", "T", "-annotate", "+850+1230", "S",

        # Word Bank Section Below Grid
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "2",
        "-draw", "roundrectangle 60,1420 1020,1830 28,28",
        "-stroke", "none",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#334155",
        "-gravity", "northwest", "-annotate", "+100+1460", "WORD BANK (3 of 5 Found):",

        # Found Chips
        "-fill", "#BAE6FD", "-draw", "roundrectangle 100,1520 380,1610 20,20",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "28", "-fill", "#0369A1",
        "-gravity", "northwest", "-annotate", "+130+1545", "✓ LION",

        "-fill", "#FDE68A", "-draw", "roundrectangle 410,1520 690,1610 20,20",
        "-fill", "#B45309",
        "-gravity", "northwest", "-annotate", "+440+1545", "✓ BEAR",

        "-fill", "#A7F3D0", "-draw", "roundrectangle 720,1520 980,1610 20,20",
        "-fill", "#047857",
        "-gravity", "northwest", "-annotate", "+750+1545", "✓ DUCK",

        # Remaining Chips with Phonics Speaker
        "-fill", "#F1F5F9", "-draw", "roundrectangle 100,1640 460,1730 20,20",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "28", "-fill", "#334155",
        "-gravity", "northwest", "-annotate", "+130+1665", "🔊 TIGER",

        "-fill", "#F1F5F9", "-draw", "roundrectangle 490,1640 850,1730 20,20",
        "-gravity", "northwest", "-annotate", "+520+1665", "🔊 MONKEY",

        # Quick Print Ribbon at bottom
        "-font", "DejaVu-Sans-Bold", "-pointsize", "24", "-fill", "#0F766E",
        "-gravity", "north", "-annotate", "+0+1765", "🖨️ Tap Print button to create paper worksheets",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

def make_screenshot_4():
    # 4. Audio Phonics & Pronunciation
    out_file = os.path.join(OUTPUT_DIR, "screenshot_4_phonics.png")
    args = [
        "-size", "1080x1920", "xc:#F8FAFC",
        # Top banner
        "-fill", "#0D9488", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "60", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "AUDIO PHONICS SPEECH",
        "-font", "DejaVu-Sans", "-pointsize", "32", "-fill", "#CCFBF1",
        "-gravity", "north", "-annotate", "+0+155", "Hear every word pronounced with Text-To-Speech",

        # Hero Highlight Card
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "3",
        "-draw", "roundrectangle 60,280 1020,880 32,32",

        "-stroke", "none",
        "-fill", "#CCFBF1", "-draw", "circle 540,430 540,530",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "80", "-fill", "#0F766E",
        "-gravity", "north", "-annotate", "+0+385", "🔊",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "44", "-fill", "#0F172A",
        "-gravity", "north", "-annotate", "+0+570", "\"E - L - E - P - H - A - N - T\"",

        "-font", "DejaVu-Sans", "-pointsize", "28", "-fill", "#64748B",
        "-gravity", "north", "-annotate", "+0+640", "Tap any word in the bank to hear clear phonics audio",

        # Sound wave graphic simulation
        "-stroke", "#0D9488", "-strokewidth", "8",
        "-draw", "line 360,740 360,780",
        "-draw", "line 420,720 420,800",
        "-draw", "line 480,700 480,820",
        "-draw", "line 540,680 540,840",
        "-draw", "line 600,700 600,820",
        "-draw", "line 660,720 660,800",
        "-draw", "line 720,740 720,780",
        "-stroke", "none",

        # Card 2: Letter Case Toggle Mode
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "3",
        "-draw", "roundrectangle 60,930 1020,1330 32,32",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#0F172A",
        "-gravity", "northwest", "-annotate", "+120+980", "🔤 UPPERCASE / lowercase Toggle",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#64748B",
        "-gravity", "northwest", "-annotate", "+120+1040", "Helps early readers practice letter matching",

        # Interactive Toggle Demo
        "-fill", "#4F46E5", "-draw", "roundrectangle 120,1110 500,1230 24,24",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "38", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+160+1145", "ABC  CAPITALS",

        "-fill", "#EEF2FF", "-stroke", "#C7D2FE", "-strokewidth", "2",
        "-draw", "roundrectangle 540,1110 920,1230 24,24",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "38", "-fill", "#4338CA",
        "-gravity", "northwest", "-annotate", "+580+1145", "abc  lowercase",

        # Card 3: First-Letter Star Hint
        "-fill", "#FEF3C7", "-stroke", "#FDE68A", "-strokewidth", "3",
        "-draw", "roundrectangle 60,1380 1020,1780 32,32",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#92400E",
        "-gravity", "northwest", "-annotate", "+120+1430", "⭐ First-Letter Hint Assistance",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#B45309",
        "-gravity", "northwest", "-annotate", "+120+1490", "Stuck on a tricky word? Pulsing star glows on the starting tile!",

        # Hint preview
        "-fill", "#FFFFFF", "-draw", "circle 240,1630 240,1700",
        "-fill", "#F59E0B", "-draw", "circle 240,1630 240,1675",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "44", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+225+1605", "L",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#78350F",
        "-gravity", "northwest", "-annotate", "+350+1615", "Pulsing Glow points the way!",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

def make_screenshot_5():
    # 5. Printable PDF Worksheets
    out_file = os.path.join(OUTPUT_DIR, "screenshot_5_printables.png")
    args = [
        "-size", "1080x1920", "xc:#F8FAFC",
        # Top banner
        "-fill", "#2563EB", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "60", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "PRINT PAPER WORKSHEETS",
        "-font", "DejaVu-Sans", "-pointsize", "32", "-fill", "#DBEAFE",
        "-gravity", "north", "-annotate", "+0+155", "Real US Letter classroom worksheets & answer keys",

        # Paper Worksheet mockup (White paper with shadow)
        "-fill", "#E2E8F0", "-draw", "roundrectangle 125,295 965,1485 16,16", # shadow
        "-fill", "#FFFFFF", "-stroke", "#CBD5E1", "-strokewidth", "2",
        "-draw", "roundrectangle 120,290 960,1480 16,16",
        "-stroke", "none",

        # Header on Paper
        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#000000",
        "-gravity", "north", "-annotate", "+0+330", "WORD FINDER MAGIC",
        "-font", "DejaVu-Sans", "-pointsize", "22", "-fill", "#475569",
        "-gravity", "north", "-annotate", "+0+380", "Animal Safari • 1st Grade Worksheet",

        # Student Name & Date Lines
        "-font", "DejaVu-Sans", "-pointsize", "22", "-fill", "#000000",
        "-gravity", "northwest", "-annotate", "+160+430", "Name: __________________________   Date: _________",

        # Puzzle Grid on Paper (Black border, crisp letters)
        "-stroke", "#000000", "-strokewidth", "2", "-fill", "none",
        "-draw", "rectangle 200,480 880,1020",
        "-stroke", "#94A3B8", "-strokewidth", "1",
        "-draw", "line 336,480 336,1020",
        "-draw", "line 472,480 472,1020",
        "-draw", "line 608,480 608,1020",
        "-draw", "line 744,480 744,1020",
        "-draw", "line 200,615 880,615",
        "-draw", "line 200,750 880,750",
        "-draw", "line 200,885 880,885",

        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "40", "-fill", "#000000",
        "-gravity", "northwest",
        "-annotate", "+245+525", "L", "-annotate", "+385+525", "I", "-annotate", "+520+525", "O", "-annotate", "+655+525", "N", "-annotate", "+795+525", "A",
        "-annotate", "+245+660", "B", "-annotate", "+385+660", "E", "-annotate", "+520+660", "A", "-annotate", "+655+660", "R", "-annotate", "+795+660", "T",
        "-annotate", "+245+795", "D", "-annotate", "+385+795", "U", "-annotate", "+520+795", "C", "-annotate", "+655+795", "K", "-annotate", "+795+795", "S",
        "-annotate", "+245+930", "T", "-annotate", "+385+930", "I", "-annotate", "+520+930", "G", "-annotate", "+655+930", "E", "-annotate", "+795+930", "R",

        # Word list with checkboxes
        "-font", "DejaVu-Sans-Bold", "-pointsize", "24", "-fill", "#000000",
        "-gravity", "northwest", "-annotate", "+160+1070", "WORD LIST (Find & Check Off):",

        "-stroke", "#000000", "-strokewidth", "2", "-fill", "none",
        "-draw", "rectangle 170,1130 195,1155",
        "-draw", "rectangle 430,1130 455,1155",
        "-draw", "rectangle 690,1130 715,1155",
        "-draw", "rectangle 170,1210 195,1235",
        "-draw", "rectangle 430,1210 455,1235",
        "-draw", "rectangle 690,1210 715,1235",

        "-stroke", "none",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#000000",
        "-gravity", "northwest",
        "-annotate", "+210+1135", "LION",
        "-annotate", "+470+1135", "BEAR",
        "-annotate", "+730+1135", "DUCK",
        "-annotate", "+210+1215", "TIGER",
        "-annotate", "+470+1215", "ZEBRA",
        "-annotate", "+730+1215", "MONKEY",

        # Bottom Page 2 note
        "-font", "DejaVu-Sans", "-pointsize", "18", "-fill", "#64748B",
        "-gravity", "north", "-annotate", "+0+1420", "Page 2 includes full Teacher Answer Key with highlighted solutions",

        # Print / PDF Dialog Card
        "-fill", "#0F766E", "-draw", "roundrectangle 80,1540 1000,1810 32,32",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "38", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+1590", "🖨️ Instant Wireless Print & PDF Export",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#CCFBF1",
        "-gravity", "north", "-annotate", "+0+1655", "Works with all Android printers & Google Drive PDF",
        "-fill", "#FFFFFF", "-draw", "roundrectangle 240,1710 840,1780 20,20",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "26", "-fill", "#0F766E",
        "-gravity", "north", "-annotate", "+0+1728", "PRINT WORKSHEET NOW",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

def make_screenshot_6():
    # 6. Ad-Free Family Pass & Kid Safe
    out_file = os.path.join(OUTPUT_DIR, "screenshot_6_family_pass.png")
    args = [
        "-size", "1080x1920", "xc:#F8FAFC",
        # Top banner
        "-fill", "#0F766E", "-draw", "rectangle 0,0 1080,240",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "60", "-fill", "#FFFFFF",
        "-gravity", "north", "-annotate", "+0+70", "100% AD-FREE & KID SAFE",
        "-font", "DejaVu-Sans", "-pointsize", "32", "-fill", "#CCFBF1",
        "-gravity", "north", "-annotate", "+0+155", "Parental Gate Protection • Families Policy Compliant",

        # Crown VIP Card
        "-fill", "#FFFFFF", "-stroke", "#E2E8F0", "-strokewidth", "3",
        "-draw", "roundrectangle 60,280 1020,950 32,32",

        "-stroke", "none",
        "-fill", "#FEF3C7", "-draw", "circle 540,430 540,540",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "80", "-fill", "#F59E0B",
        "-gravity", "north", "-annotate", "+0+380", "👑",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "44", "-fill", "#0F172A",
        "-gravity", "north", "-annotate", "+0+580", "Unlimited Family Pass",

        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#64748B",
        "-gravity", "north", "-annotate", "+0+645", "Everything unlocked forever for stress-free learning",

        # Feature bullet list
        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#0F766E",
        "-gravity", "northwest",
        "-annotate", "+120+720", "✓  Unlimited Puzzles across all 7 grades",
        "-annotate", "+120+775", "✓  Unlimited Printable PDF Worksheets",
        "-annotate", "+120+830", "✓  Custom Theme & Weekly Spelling Creator",
        "-annotate", "+120+885", "✓  100% Ad-Free • No In-App Distractions",

        # Parental Gate Card (Highlighting safety!)
        "-fill", "#FEF2F2", "-stroke", "#FECACA", "-strokewidth", "3",
        "-draw", "roundrectangle 60,990 1020,1380 32,32",
        "-stroke", "none",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "34", "-fill", "#991B1B",
        "-gravity", "northwest", "-annotate", "+120+1040", "🔒 Kid-Safe Parental Gate",
        "-font", "DejaVu-Sans", "-pointsize", "26", "-fill", "#B91C1C",
        "-gravity", "northwest", "-annotate", "+120+1095", "Children cannot make accidental purchases without parent approval.",

        "-fill", "#FFFFFF", "-stroke", "#FCA5A5", "-strokewidth", "2",
        "-draw", "roundrectangle 120,1160 960,1320 20,20",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#7F1D1D",
        "-gravity", "northwest", "-annotate", "+160+1200", "Grown-Ups Only: What is 7 + 8 = ?",
        "-font", "DejaVu-Sans", "-pointsize", "24", "-fill", "#059669",
        "-gravity", "northwest", "-annotate", "+160+1255", "✓ Verified Parent Access",

        # Pricing Card
        "-fill", "#F0FDF4", "-stroke", "#86EFAC", "-strokewidth", "3",
        "-draw", "roundrectangle 60,1430 1020,1820 32,32",
        "-stroke", "none",

        "-font", "DejaVu-Sans-Bold", "-pointsize", "36", "-fill", "#14532D",
        "-gravity", "northwest", "-annotate", "+120+1480", "Simple, Transparent Family Pricing",

        "-fill", "#15803D", "-draw", "roundrectangle 120,1550 520,1750 24,24",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#FFFFFF",
        "-gravity", "northwest", "-annotate", "+150+1590", "Lifetime Pass",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "42", "-fill", "#BBF7D0",
        "-gravity", "northwest", "-annotate", "+150+1635", "$2.99",
        "-font", "DejaVu-Sans", "-pointsize", "22", "-fill", "#DCFCE7",
        "-gravity", "northwest", "-annotate", "+150+1695", "One-time purchase",

        "-fill", "#FFFFFF", "-stroke", "#86EFAC", "-strokewidth", "2",
        "-draw", "roundrectangle 560,1550 960,1750 24,24",
        "-stroke", "none",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "30", "-fill", "#14532D",
        "-gravity", "northwest", "-annotate", "+590+1590", "Monthly Pass",
        "-font", "DejaVu-Sans-Bold", "-pointsize", "42", "-fill", "#15803D",
        "-gravity", "northwest", "-annotate", "+590+1635", "$0.99",
        "-font", "DejaVu-Sans", "-pointsize", "22", "-fill", "#64748B",
        "-gravity", "northwest", "-annotate", "+590+1695", "Per month",

        out_file
    ]
    run_im(args)
    print("Created:", out_file)

if __name__ == "__main__":
    make_screenshot_1()
    make_screenshot_2()
    make_screenshot_3()
    make_screenshot_4()
    make_screenshot_5()
    make_screenshot_6()
    print("All 6 Google Play Store screenshots generated successfully!")
