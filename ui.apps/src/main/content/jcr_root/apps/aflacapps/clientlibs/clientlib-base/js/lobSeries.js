var LOBseries = [
{
    "LOB": "Accident,Accident-High,Accident-Mid,Accident-Low,Accident-High-LT,Accident-Mid-LT,Accident-Low-LT",
    "Series": ["7700", "7800", "70000"]
}, {
    "LOB": "Hospital Indemnity,Hospital Indemnity-High,Hospital Indemnity-Mid,Hospital Indemnity-Low,Hospital Indemnity-High-LT,Hospital Indemnity-Mid-LT,Hospital Indemnity-Low-LT",
    "Series": ["8500", "8800", "80000"]
}, {
    "LOB": "BenExtend,BenExtend-High,BenExtend-Mid,BenExtend-Low,BenExtend-High-LT,BenExtend-Mid-LT,BenExtend-Low-LT",
    "Series": ["81000"]
}, {
    "LOB": "Critical Illness,Critical Illness-With Cancer,Critical Illness-Without Cancer,Critical Illness-With Health Screening Benefit,Critical Illness-Without Health Screening Benefit",
    "Series": ["20000", "2100", "2800", "21000", "22000"],
    "ageCalculation": ["Issue Age", "Attained Age"]
}, {
    "LOB": "Disability",
    "Series": ["5000", "50000"],
    "ageCalculation": ["Issue Age"]
}, {
    "LOB": "Whole Life",
    "Series": ["9800", "60000"]
}, {
    "LOB": "Term Life",
    "Series": ["9100"]
}, {
    "LOB": "Term to 120",
    "Series": ["93000"],
    "ageCalculation": ["Issue Age"]
},
{
    "LOB": "Dental",
    "Series": ["1100"]
} 
];

var accidentLOBs = ["Accident","Accident-High","Accident-Mid", "Accident-Low","Accident-High-LT","Accident-Mid-LT","Accident-Low-LT"] ;
var hospitalIndemnityLOBs = ["Hospital Indemnity","Hospital Indemnity-High","Hospital Indemnity-Mid","Hospital Indemnity-Low","Hospital Indemnity-High-LT","Hospital Indemnity-Mid-LT","Hospital Indemnity-Low-LT"];
var benExtendLOBs = ["BenExtend","BenExtend-High","BenExtend-Mid","BenExtend-Low","BenExtend-High-LT","BenExtend-Mid-LT","BenExtend-Low-LT"];
var criticalIllnessLOBs = ["Critical Illness","Critical Illness-With Cancer","Critical Illness-Without Cancer","Critical Illness-With Health Screening Benefit","Critical Illness-Without Health Screening Benefit"];
var disabilityLOBs = ["Disability"];
var wholeLifeLOBs = ["Whole Life"];
var termLifeLOBs = ["Term Life"];
var termto120LOBs = ["Term to 120"];

var masterAppAccident=["AC7700","AC7800","AC70000"];
var masterHospitalIndemnity=["HI8500","HI8800","HI80000","HI81000"];
var masterCriticalIllness=["CI2100","CI2800","CI20000","CI21000","CI22000"];
var masterDisability=["DI5000","DI50000"];
var masterDental=["D1100"];
var masterTermLife=["TL9100"];
var masterTermTo120 = ["TL93000"];
var masterWholeLife=["WL9800","WL60000"];

var dedFreqDefaultOptions = ['Annually', 'Monthly', 'Semi-Monthly', 'Bi-Weekly', 'Weekly'];

var masterAppLOBdata = [
    {
        "LOB": "Accident",
        "Series": ["7700", "7800", "70000"],
        "ProductPrefix": "AC"
    }, {
        "LOB": "Hospital Indemnity",
        "Series": ["8500", "8800", "80000", "81000"],
        "ProductPrefix": "HI"
    }, {
        "LOB": "Critical Illness",
        "Series": ["20000", "2100", "2800", "21000", "22000"],
        "ageCalculation": ["Issue Age", "Attained Age"],
        "ProductPrefix": "CI"
    }, {
        "LOB": "Disability",
        "Series": ["5000", "50000"],
        "ProductPrefix": "DI"
    }, {
        "LOB": "Whole Life",
        "Series": ["9800", "60000"],
        "ProductPrefix": "WL"
    }, {
        "LOB": "Term Life",
        "Series": ["9100"],
        "ProductPrefix": "TL"
    }, {
        "LOB": "Term to 120",
        "Series": ["93000"],
        "ProductPrefix": "TL"
    }, {
        "LOB": "Dental",
        "Series": ["1100"],
        "ProductPrefix": "D"
    } 
];