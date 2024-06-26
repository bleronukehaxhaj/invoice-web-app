<script setup>

</script>

<template>
  <div class="container-fluid px-0 custom-hero-section py-5 bg-light" style="">
    <div class="row mx-0">

      <div class="col-lg-6 d-flex align-items-center">
        <div class="px-4">
          <h1 class="display-5 fw-bold text-dark">Invoice Your Customers in Seconds</h1>
          <p class="lead mb-4">
            The world's simplest way to invoice customers, from your phone or laptop. Save time, stay organized and look
            professional.
            For contractors, freelancers, owner-operators, creatives, and other small business owners.
          </p>
          <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
            <router-link :to="{name:'invoice-create'}"
                         active-class="active"
                         class="btn btn-lg btn-dark px-4">
              Create An Invoice Now
            </router-link>
          </div>
        </div>
      </div>


      <div class="col-lg-6 d-flex align-items-center justify-content-end">
        <img class="img-fluid" src="../../assets/img7.webp" alt="Your Image"
             style="max-width: 100%; height: auto;">
      </div>
    </div>
  </div>

  <div style="background-color: #F4F8F9">
    <div class="container py-5">

      <div class="row justify-content-around">
        <h1 class="display-5 fw-bold text-body-emphasis text-center mb-5">
          The Easiest Invoicing Software You’ll Ever Use</h1>

        <div class="col-lg-3 mb-5 mt-5">
          <div class="card border-0 shadow">
            <div class="card-body text-center">
              <i class="bi bi-clock-fill text-dark" style="font-size: 3rem;"></i>
              <h5 class="card-title text-dark">Save Time</h5>
              <p class="card-text">Quickly create professional invoices and estimates wherever you are—with a customer,
                between jobs, or at home. Convert estimates to invoices with one click.</p>
            </div>
          </div>
        </div>

        <div class="col-lg-3 mb-5 mt-5">
          <div class="card border-0 shadow">
            <div class="card-body text-center">
              <i class="bi bi-currency-dollar mb-5 text-dark" style="font-size: 3rem;"></i>
              <h5 class="card-title text-dark">Get Paid</h5>
              <p class="card-text">Email, text or print your invoices. Get notified when invoices are read and stay on
                top of late payments. Accept credit card payments as well as cash, check and others.</p>
            </div>
          </div>
        </div>


        <div class="col-lg-3 mb-5 mt-5">
          <div class="card border-0 shadow">
            <div class="card-body text-center">
              <i class="bi bi-check-circle-fill mb-5 text-dark" style="font-size: 3rem;"></i>
              <h5 class="card-title text-dark">Look Professional</h5>
              <p class="card-text">Simply choose an invoice template and customize it with your logo. Add a signature,
                photos, notes and more. Always look professional and stay organized.</p>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>

  <div class="container my-5">
    <div class="row mb-4">
      <div class="col text-center">
        <h1 class="text-dark">Featured in</h1>
      </div>
    </div>

    <div class="row justify-content-center align-items-center">
      <div class="col-lg-2 col-md-3 col-6 mb-4">
        <img src="../../assets/img1.webp" alt="Logo 1" class="img-fluid">
      </div>

      <div class="col-lg-2 col-md-3 col-6 mb-4">
        <img src="../../assets/img2.webp" alt="Logo 2" class="img-fluid">
      </div>

      <div class="col-lg-2 col-md-3 col-6 mb-4">
        <img src="../../assets/img3.webp" alt="Logo 3" class="img-fluid">
      </div>
      <div class="col-lg-2 col-md-3 col-6 mb-4">
        <img src="../../assets/img4.webp" alt="Logo 3" class="img-fluid">
      </div>
      <div class="col-lg-2 col-md-3 col-6 mb-4">
        <img src="../../assets/img5.webp" alt="Logo 3" class="img-fluid">
      </div>

    </div>
  </div>

  <div class="container my-5">
    <div class="row">
      <div class="col-md-6">
        <h2 class="mb-5 text-dark">Online Invoice Generator</h2>
        <p>
          Use our invoice generator to manage your invoicing from any device, anytime. Your account is always connected
          and your data is saved securely for you.Create your first invoice, and your details are securely stored and
          auto-populated on future projects. Work on the go? It does too. Use your mobile device to create and share
          invoices.
        </p>
      </div>

      <div class="col-md-6">
        <img src="../../assets/img8.jpg" alt="Image" class="img-fluid">
      </div>
    </div>
  </div>

  <div class="container-fluid d-flex justify-content-center align-items-center bg-body-secondary"
       style="height: 50vh">
    <div class="row justify-content-center">
      <div class="col-md-8 text-center">
        <h2 class="mb-4 text-dark">Invoice Simple</h2>
        <h5 class="mb-4 text-dark">
          Create, send and manage invoices online with Invoice Simple
        </h5>
        <a href="#" class="btn btn-lg btn-outline-dark mb-4">Get Started</a>
      </div>
    </div>
  </div>

  <footer class="d-flex flex-wrap justify-content-between align-items-center py-5 border-top">
    <div class="col-md-4 d-flex align-items-center">
      <span class="ms-5 mb-3 mb-md-0 text-body-secondary fs-6">© 2024 Company, Inc</span>
    </div>

    <ul class="nav col-md-4 justify-content-end d-flex me-5">
      <li class="ms-3"><a class="text-body-secondary fs-4" href="#"><i class="bi bi-facebook"></i></a></li>
      <li class="ms-3"><a class="text-body-secondary fs-4" href="#"><i class="bi bi-instagram"></i></a></li>
      <li class="ms-3"><a class="text-body-secondary fs-4" href="#"><i class="bi bi-twitter-x"></i></a></li>
      <li class="ms-3"><a class="text-body-secondary fs-4" href="#"><i class="bi bi-linkedin"></i></a></li>
    </ul>
  </footer>
</template>

<style scoped>

</style>