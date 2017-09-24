<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app = "profil">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="${pageContext.request.contextPath}/js/angular.min.js"></script>
<script src="${pageContext.request.contextPath}/js/profil.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">


<title>Insert title here</title>
</head>
<body ng-controller="appController" ng-init = "getPrijateljiPoStranama('${userDetails.username }', 10, 1); ucitajAktivnosti(3,1,'${userDetails.username}'); podesiDugmePrati('${userDetails.username}'); ">

    <center>
	<h1><a href="/trcanje" id="naslov">Trcko</a></h1>
		
	

Ime: ${userDetails.ime } </br>
Prezime: ${userDetails.prezime } </br>
Korisnicko ime: ${userDetails.username }</br>
<div class="prati" ng-if="ulogovaniKorisnik.username != '${userDetails.username}'"><input type="button" id="btn_prati" value="prati korisnika ${userDetails.username }" ng-click="prati(ulogovaniKorisnik.username,'${userDetails.username }')"></button> </div> </br> 
<div class="profil_slika"> <img src="/trcanje/services/rest/profilna/${userDetails.username}?imeSlike=${userDetails.slika}" alt="${userDetails.username }" height="200px"> </img> </div>
<br/>

</center>
<hr/>
<center>

<div ng-if="aktivnosti.length == 0">
<p>Nemas ni jednu aktivnost</p>
</div>

<div class="wrapper_profil_glavni" >
<div class="aktivnosti_glavni" ng-repeat="aktivnost in aktivnosti">
		<div class="pomocni_profil">
		<center>
			<p class="naslov">{{aktivnost.aktivnost.naslov}} </p>
			<p><img alt="{{aktivnost.korisnik.username}}" ng-src="/trcanje/services/rest/profilna/{{aktivnost.korisnik.username}}?imeSlike={{aktivnost.korisnik.slika}}" height="50px"> </img> </p><!-- aktivnost.slika je slika korisnika koji je obavio aktivnost -->
			<p><a href="#" ng-click = "otvoriProfil(aktivnost.korisnik.username)" >{{aktivnost.korisnik.username}} </a></p>
			<br />
			<!-- TODO: da pise ime i prezime -->
			<p class="datum">{{aktivnost.aktivnost.datumFormatiran}} </p>
			<p>
			{{aktivnost.aktivnost.tempo}}/km  {{aktivnost.aktivnost.distanca}} km</p>
			<p>Vreme: {{aktivnost.aktivnost.trajanje}}</p>
			<br />
			<table><tr><th><input type="image" src="${pageContext.request.contextPath}/slike/lajk.png" height="25px" ng-click="lajkuj(ulogovaniKorisnik.username, aktivnost.aktivnost.id);"/></th><th>{{aktivnost.brojLajkova}}</th></tr></table>
		</center>
		</div>
		<br/><br/>
	</div>
		<div ng-if="(aktivnosti.length < ukupanBrojAktivnosti) && aktivnosti.length > 0"><center><a href="#!" ng-click="ucitajJos('${userDetails.username }')">Ucitaj jos</a></center></div>
	<br/>
	
</div>
	<div class = "pratimPratioci" ng-if="prijatelji.length > 0 || pratioci.length > 0">
	
	<div class="table_prijatelji_wrapper">
	
	<table style="display:inline-block">
	<tr>
	<td>Pratim</td>
	</tr>
	<tr ng-repeat="korisnik in prijatelji">
		 <td> <img ng-src="/trcanje/services/rest/profilna/{{korisnik.username}}?imeSlike={{korisnik.slika}}" height="35px"/> </td> <td><a ng-click = "otvoriProfil(korisnik.username)" href=""> {{korisnik.ime}} {{korisnik.prezime}} </a></td>
	</tr>
	</table>

	</div>

	<div class="table_pratioci_wrapper">

	<table style="float:right">
	<tr>
	<td>Pratioci</td>
	</tr>
	<tr ng-repeat="korisnik in pratioci">
		<td> <img ng-src="/trcanje/services/rest/profilna/{{korisnik.username}}?imeSlike={{korisnik.slika}}" height="35px"/> </td> <td><a ng-click = "otvoriProfil(korisnik.username)" href=""> {{korisnik.ime}} {{korisnik.prezime}} </a></td>
	</tr>
	</table>
	
	</div>
	
</div>

<div class = "pratimPratioci" ng-if="prijatelji.length == 0 && pratioci.length == 0">
<p>Ne pratis nikog i niko te ne prati</p>
</div>

</center>




</body>
</html>