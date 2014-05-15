﻿using OnTheMove.Lib;
using OnTheMove.Lib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace OnTheMove.Controllers
{
    public class PartidasController : ApiController
    {
        //
        // GET: /Voos/

        public IEnumerable<Voo> Get(string id)
        {
            return Lib_Voo.ListPartidas(id);
        }

    }
}